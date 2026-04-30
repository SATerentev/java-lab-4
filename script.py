import sqlite3

def main():
    db = 'back\\src\\main\\resources\\data.db'
    file_name = 'Вопросы.txt'

    connection = sqlite3.connect(db)
    cursor = connection.cursor()

    cursor.execute('''
        CREATE TABLE IF NOT EXISTS questions (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                text TEXT,
                answer1 TEXT,
                answer2 TEXT,
                answer3 TEXT,
                answer4 TEXT,
                correct_answer INTEGER,
                grade INTEGER
            )
    ''')

    with open(file_name, encoding='UTF-8') as file:
        for question in file:
            parts = question.split('\t')
            text = parts[0]
            answer1 = parts[1]
            answer2 = parts[2]
            answer3 = parts[3]
            answer4 = parts[4]
            correct_answer = int(parts[5])
            grade = int(parts[6])

            cursor.execute('INSERT INTO questions ' + 
                           '(text, answer1, answer2, answer3, answer4, correct_answer, grade)' +
                            ' VALUES (?, ?, ?, ?, ?, ?, ?)', 
                            (text, answer1, answer2, answer3, answer4, correct_answer, grade))
    
    connection.commit()
    connection.close()


if __name__ == '__main__':
    main()