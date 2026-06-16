#!/usr/bin/env bash
set -Eeuo pipefail

APP_NAME="${APP_NAME:-mytutor}"
SERVICE_DIR="${SERVICE_DIR:-/tutor/service}"
ENV_FILE="${ENV_FILE:-${SERVICE_DIR}/${APP_NAME}.env}"

log() {
  printf '[%s] %s\n' "$(date '+%Y-%m-%d %H:%M:%S')" "$*"
}

die() {
  log "错误: $*" >&2
  exit 1
}

load_env() {
  if [[ -f "$ENV_FILE" ]]; then
    set -a
    # shellcheck disable=SC1090
    . "$ENV_FILE"
    set +a
  else
    die "环境变量文件不存在: ${ENV_FILE}。"
  fi
}

set_runtime_defaults() {
  SERVICE_DIR="${SERVICE_DIR:-/tutor/service}"
  JAR_FILE="${JAR_FILE:-${SERVICE_DIR}/MY_TUTOR-0.0.1-SNAPSHOT.jar}"
  LOG_DIR="${LOG_DIR:-${SERVICE_DIR}/log}"
  PID_FILE="${PID_FILE:-${SERVICE_DIR}/${APP_NAME}.pid}"
}

validate_start_config() {
  [[ -n "${STRIPE_API_KEY:-}" ]] || die "必须配置 STRIPE_API_KEY，支付模块才能启动。"
  [[ -n "${STRIPE_WEBHOOK_SECRET:-}" ]] || die "必须配置 STRIPE_WEBHOOK_SECRET，才能校验 Stripe Webhook。"

  if [[ "${STRIPE_MODE:-test}" == "live" && "${STRIPE_API_KEY}" != sk_live_* ]]; then
    die "STRIPE_MODE=live 时 STRIPE_API_KEY 必须以 sk_live_ 开头。"
  fi

  if [[ "${STRIPE_WEBHOOK_SECRET}" != whsec_* ]]; then
    die "STRIPE_WEBHOOK_SECRET 必须以 whsec_ 开头。"
  fi

  [[ -f "$JAR_FILE" ]] || die "JAR 文件不存在: $JAR_FILE"
}

read_pid() {
  [[ -f "$PID_FILE" ]] || return 1
  local pid
  pid="$(tr -d '[:space:]' < "$PID_FILE")"
  [[ "$pid" =~ ^[0-9]+$ ]] || return 1
  printf '%s' "$pid"
}

is_pid_running() {
  local pid="$1"
  kill -0 "$pid" 2>/dev/null
}

running_pid() {
  local pid
  if pid="$(read_pid)" && is_pid_running "$pid"; then
    printf '%s' "$pid"
    return 0
  fi
  return 1
}

cleanup_stale_pid() {
  if [[ -f "$PID_FILE" ]] && ! running_pid >/dev/null; then
    rm -f "$PID_FILE"
  fi
}

wait_for_start() {
  local pid="$1"
  local logfile="$2"
  local waited=0

  while (( waited < 30 )); do
    if ! is_pid_running "$pid"; then
      log "进程在启动过程中退出，最后日志如下:"
      tail -n 80 "$logfile" 2>/dev/null || true
      exit 1
    fi

    if (( waited >= 3 )); then
      log "${APP_NAME} 已启动。pid=$pid log=$logfile"
      return 0
    fi

    sleep 1
    waited=$((waited + 1))
  done

  log "${APP_NAME} 启动等待已超过 30 秒，进程仍在运行。pid=$pid log=$logfile"
}

start_app() {
  load_env
  set_runtime_defaults
  cleanup_stale_pid
  validate_start_config

  local pid
  if pid="$(running_pid)"; then
    log "${APP_NAME} 已经在运行。pid=$pid"
    return 0
  fi

  mkdir -p "$LOG_DIR" "$(dirname "$PID_FILE")"

  local timestamp logfile
  timestamp="$(date '+%Y%m%d-%H%M%S')"
  logfile="${LOG_DIR}/${APP_NAME}-${timestamp}.log"

  log "正在启动 ${APP_NAME}，JAR=$JAR_FILE"
  nohup java -jar "$JAR_FILE" > "$logfile" 2>&1 &
  pid="$!"
  printf '%s\n' "$pid" > "$PID_FILE"

  wait_for_start "$pid" "$logfile"
}

stop_app() {
  set_runtime_defaults

  local pid
  if ! pid="$(running_pid)"; then
    cleanup_stale_pid
    log "${APP_NAME} 当前未运行。"
    return 0
  fi

  log "正在停止 ${APP_NAME}。pid=$pid"
  kill "$pid" 2>/dev/null || true

  local waited=0
  while (( waited < 30 )); do
    if ! is_pid_running "$pid"; then
      rm -f "$PID_FILE"
      log "${APP_NAME} 已停止。"
      return 0
    fi
    sleep 1
    waited=$((waited + 1))
  done

  log "${APP_NAME} 在 30 秒内未停止，执行强制停止。"
  kill -KILL "$pid" 2>/dev/null || true
  rm -f "$PID_FILE"
  log "${APP_NAME} 已停止。"
}

stop_app
start_app
