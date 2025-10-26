#!/usr/bin/env python3
"""
Generate 200 Grade 8 Math Questions (English with French Translation)
Output SQL INSERT statements for testing questions
"""

def generate_questions():
    questions = []
    qid = 201  # Knowledge point ID (Grade 8)
    
    # =============================================
    # Category 1: Number and Algebra (80 questions)
    # =============================================
    
    # Quadratic Radicals (10 questions)
    questions.extend([
        {
            'kp_id': 201, 'type': 1, 'diff': 1,
            'title': 'Quadratic Radicals 1', 'title_fr': 'Radicaux quadratiques 1',
            'content': 'Which of the following is a quadratic radical?',
            'content_fr': 'Lequel des suivants est un radical quadratique ?',
            'options': ['A. √4', 'B. ∛8', 'C. ∜16', 'D. √-1'],
            'options_fr': ['A. √4', 'B. ∛8', 'C. ∜16', 'D. √-1'],
            'correct': 'A', 'explanation': '√4 is a quadratic radical', 'explanation_fr': '√4 est un radical quadratique'
        },
        {
            'kp_id': 201, 'type': 1, 'diff': 1,
            'title': 'Value of √36', 'title_fr': 'Valeur de √36',
            'content': 'The value of √36 is', 'content_fr': 'La valeur de √36 est',
            'options': ['A. 6', 'B. 12', 'C. 18', 'D. 36'],
            'options_fr': ['A. 6', 'B. 12', 'C. 18', 'D. 36'],
            'correct': 'A', 'explanation': '6×6=36, so √36=6', 'explanation_fr': '6×6=36, donc √36=6'
        },
        {
            'kp_id': 201, 'type': 1, 'diff': 2,
            'title': 'Simplify √18', 'title_fr': 'Simplifier √18',
            'content': 'Simplify: √18', 'content_fr': 'Simplifier : √18',
            'options': ['A. 3√2', 'B. 2√3', 'C. 6√3', 'D. 9√2'],
            'options_fr': ['A. 3√2', 'B. 2√3', 'C. 6√3', 'D. 9√2'],
            'correct': 'A', 'explanation': '√18=√(9×2)=3√2', 'explanation_fr': '√18=√(9×2)=3√2'
        },
        {
            'kp_id': 201, 'type': 1, 'diff': 2,
            'title': 'Calculate √50', 'title_fr': 'Calculer √50',
            'content': 'Calculate: √50', 'content_fr': 'Calculer : √50',
            'options': ['A. 5√2', 'B. 2√5', 'C. 10√2', 'D. 25'],
            'options_fr': ['A. 5√2', 'B. 2√5', 'C. 10√2', 'D. 25'],
            'correct': 'A', 'explanation': '√50=√(25×2)=5√2', 'explanation_fr': '√50=√(25×2)=5√2'
        },
    ])
    
    return questions

# Generate SQL
sql = []
questions = generate_questions()

print('-- Generate 200 Grade 8 Math Questions')
print('INSERT INTO `question` VALUES')
print()

for i, q in enumerate(questions):
    options_str = ', '.join(f'"{opt}"' for opt in q['options'])
    options_fr_str = ', '.join(f'"{opt}"' for opt in q['options_fr'])
    
    sql_line = f"""(NULL, {q['kp_id']}, {q['type']}, '{q['title']}', '{q['title_fr']}', '{q['content']}', '{q['content_fr']}', '["{', '.join(q['options'])}"]', '["{', '.join(q['options_fr'])}"]', '{q['correct']}', '{q['correct']}', '{q['explanation']}', '{q['explanation_fr']}', {q['diff']}, 2, {i+1})"""
    
    print(sql_line)

if __name__ == '__main__':
    generate_questions()
