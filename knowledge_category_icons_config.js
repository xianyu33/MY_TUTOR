/**
 * 知识点分类图标配置文档
 * 包含图片URL和Element UI图标类名
 */

// 知识点分类图标配置
export const knowledgeCategoryIcons = {
  // 1. 数与代数 - Number and Algebra
  NUMBER_ALGEBRA: {
    name: '数与代数',
    nameFr: 'Nombres et Algèbre',
    code: 'NUMBER_ALGEBRA',
    sortOrder: 1,
    
    // 图片资源
    imageUrl: '/images/category/numbers-algebra.png',
    
    // Element UI图标类名
    iconClass: 'el-icon-s-data', // 数据图表图标
    
    // 备用图标（Element UI）
    alternativeIcons: [
      'el-icon-calculator',      // 计算器
      'el-icon-s-opportunity',   // 机会
      'el-icon-s-marketing'      // 营销
    ],
    
    // CSS颜色主题
    colorTheme: {
      primary: '#409EFF',  // 蓝色
      secondary: '#79BBFF'
    }
  },
  
  // 2. 几何 - Geometry
  GEOMETRY: {
    name: '几何',
    nameFr: 'Géométrie',
    code: 'GEOMETRY',
    sortOrder: 2,
    
    // 图片资源
    imageUrl: '/images/category/geometry.png',
    
    // Element UI图标类名
    iconClass: 'el-icon-s-grid', // 网格图标
    
    // 备用图标（Element UI）
    alternativeIcons: [
      'el-icon-rank',             // 排名
      'el-icon-view',             // 查看
      'el-icon-copy-document'     // 文档
    ],
    
    // CSS颜色主题
    colorTheme: {
      primary: '#67C23A',  // 绿色
      secondary: '#95D475'
    }
  },
  
  // 3. 统计与概率 - Statistics and Probability
  STATISTICS_PROBABILITY: {
    name: '统计与概率',
    nameFr: 'Statistiques et Probabilité',
    code: 'STATISTICS_PROBABILITY',
    sortOrder: 3,
    
    // 图片资源
    imageUrl: '/images/category/statistics.png',
    
    // Element UI图标类名
    iconClass: 'el-icon-s-data', // 数据统计图标
    
    // 备用图标（Element UI）
    alternativeIcons: [
      'el-icon-s-marketing',      // 营销统计
      'el-icon-s-shop',           // 商店
      'el-icon-news'              // 新闻
    ],
    
    // CSS颜色主题
    colorTheme: {
      primary: '#E6A23C',  // 橙色
      secondary: '#EEBE77'
    }
  },
  
  // 4. 综合应用 - Comprehensive Application
  COMPREHENSIVE: {
    name: '综合应用',
    nameFr: 'Application Intégrée',
    code: 'COMPREHENSIVE',
    sortOrder: 4,
    
    // 图片资源
    imageUrl: '/images/category/comprehensive.png',
    
    // Element UI图标类名
    iconClass: 'el-icon-s-opportunity', // 机会综合图标
    
    // 备用图标（Element UI）
    alternativeIcons: [
      'el-icon-s-help',            // 帮助
      'el-icon-s-custom',          // 自定义
      'el-icon-s-tools'            // 工具
    ],
    
    // CSS颜色主题
    colorTheme: {
      primary: '#F56C6C',  // 红色
      secondary: '#F89898'
    }
  }
};

/**
 * 使用示例 - Vue组件
 */
export default {
  data() {
    return {
      categories: [
        {
          id: 1,
          ...knowledgeCategoryIcons.NUMBER_ALGEBRA,
          totalPoints: 13,
          progress: 38.46
        },
        {
          id: 2,
          ...knowledgeCategoryIcons.GEOMETRY,
          totalPoints: 10,
          progress: 25.00
        },
        {
          id: 3,
          ...knowledgeCategoryIcons.STATISTICS_PROBABILITY,
          totalPoints: 3,
          progress: 0.00
        },
        {
          id: 4,
          ...knowledgeCategoryIcons.COMPREHENSIVE,
          totalPoints: 4,
          progress: 50.00
        }
      ]
    };
  }
};
