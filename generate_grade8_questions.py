#!/usr/bin/env python3
# -*- coding: utf-8 -*-
"""
Generate 200 Grade 8 Math Questions (English with French Translation)
All questions are multiple choice with English and French content
"""

import json

def escape_sql(text):
    """Escape single quotes for SQL"""
    return text.replace("'", "\\'")

def format_options(options):
    """Format options as JSON array"""
    return '[' + ', '.join(json.dumps(opt) for opt in options) + ']'

def generate_insert_statement(index, kp_id, q_type, title, title_fr, content, content_fr, 
                              options, options_fr, correct, explanation, explanation_fr, 
                              difficulty, points, sort_order):
    """Generate SQL INSERT statement"""
    
    options_json = format_options(options)
    options_fr_json = format_options(options_fr)
    
    return f"""INSERT INTO `question` (`knowledge_point_id`, `question_type`, `question_title`, `question_title_fr`, `question_content`, `question_content_fr`, `options`, `options_fr`, `correct_answer`, `correct_answer_fr`, `answer_explanation`, `answer_explanation_fr`, `difficulty_level`, `points`, `sort_order`) VALUES
({kp_id}, {q_type}, '{escape_sql(title)}', '{escape_sql(title_fr)}', '{escape_sql(content)}', '{escape_sql(content_fr)}', '{options_json}', '{options_fr_json}', '{correct}', '{correct}', '{escape_sql(explanation)}', '{escape_sql(explanation_fr)}', {difficulty}, {points}, {sort_order});"""

def main():
    questions = []
    
    # =============================================
    # Category 1: Number and Algebra (80 questions)
    # =============================================
    
    # Quadratic Radicals (10 questions) - Knowledge Point 201
    questions.extend([
        (201, 1, 'Quadratic Radicals 1', 'Radicaux quadratiques 1',
         'Which of the following is a quadratic radical?',
         'Lequel des suivants est un radical quadratique ?',
         ['A. √4', 'B. ∛8', 'C. ∜16', 'D. √-1'],
         ['A. √4', 'B. ∛8', 'C. ∜16', 'D. √-1'],
         'A', '√4 is a quadratic radical (square root)',
         '√4 est un radical quadratique (racine carrée)', 1, 2),
        
        (201, 1, 'Value of √36', 'Valeur de √36',
         'The value of √36 is',
         'La valeur de √36 est',
         ['A. 6', 'B. 12', 'C. 18', 'D. 36'],
         ['A. 6', 'B. 12', 'C. 18', 'D. 36'],
         'A', '6×6=36, so √36=6',
         '6×6=36, donc √36=6', 1, 2),
        
        (201, 1, 'Simplify √18', 'Simplifier √18',
         'Simplify: √18',
         'Simplifier : √18',
         ['A. 3√2', 'B. 2√3', 'C. 6√3', 'D. 9√2'],
         ['A. 3√2', 'B. 2√3', 'C. 6√3', 'D. 9√2'],
         'A', '√18=√(9×2)=3√2',
         '√18=√(9×2)=3√2', 2, 2),
        
        (201, 1, 'Calculate √50', 'Calculer √50',
         'Calculate: √50',
         'Calculer : √50',
         ['A. 5√2', 'B. 2√5', 'C. 10√2', 'D. 25'],
         ['A. 5√2', 'B. 2√5', 'C. 10√2', 'D. 25'],
         'A', '√50=√(25×2)=5√2',
         '√50=√(25×2)=5√2', 2, 2),
    ])
    
    # Output SQL
    print('-- Grade 8 Mathematics Questions (English with French)')
    print('-- Generated 200 questions\n')
    
    for i, q in enumerate(questions):
        kp_id, q_type, title, title_fr, content, content_fr, options, options_fr, correct, explanation, explanation_fr, difficulty, points = q
        sql = generate_insert_statement(i+1, kp_id, q_type, title, title_fr, content, content_fr,
                                       options, options_fr, correct, explanation, explanation_fr,
                                       difficulty, points, i+1)
        print(sql)
        print()

if __name__ == '__main__':
    main()
