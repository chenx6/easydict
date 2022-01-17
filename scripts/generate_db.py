"""
Minimal version dataset:
sqlite> select count(*) from dict where collins != '' or oxford = 1 or tag != '' or bnc != 0 or frq != 0;
59137
"""
from sqlite3 import connect, IntegrityError, Connection
from csv import reader

from tqdm import tqdm

# from stardict import stripword

def stripword(word):
    return (''.join([ n for n in word if n.isalnum() ])).lower()


def create_db(conn: Connection):
    conn.executescript(
        """
        CREATE TABLE IF NOT EXISTS "dict" (
            "id" INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
            "word" TEXT COLLATE NOCASE NOT NULL UNIQUE,
            "sw" TEXT COLLATE NOCASE NOT NULL,
            "phonetic" TEXT,
            "definition" TEXT,
            "translation" TEXT,
            "pos" TEXT,
            "collins" INTEGER DEFAULT(0),
            "oxford" INTEGER DEFAULT(0),
            "tag" TEXT,
            "bnc" INTEGER DEFAULT(NULL),
            "frq" INTEGER DEFAULT(NULL),
            "exchange" TEXT,
            "detail" TEXT,
            "audio" TEXT
        );
        CREATE UNIQUE INDEX IF NOT EXISTS "index_dict_word" ON dict (word);
        CREATE INDEX IF NOT EXISTS "index_dict_sw_word" ON dict (sw, word collate nocase);"""
    )
    conn.commit()


def import_csv_sqlite(conn: Connection):
    with open("ecdict.csv", newline="") as csvfile:
        r = reader(csvfile)
        cursor = conn.cursor()
        is_fl = True
        for row in tqdm(r):
            if is_fl:
                is_fl = False
                continue
            sw = stripword(row[0])
            row.insert(1, sw)
            try:
                cursor.execute(
                    """
                    INSERT INTO dict (word, sw, phonetic, definition, translation, pos, collins, oxford, tag, bnc, frq, exchange, detail, audio)
                    VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)""",
                    row,
                )
            except IntegrityError as e:
                print(e, row)
        conn.commit()


def migrate(conn: Connection):
    conn.executescript(
        """
        CREATE TABLE wordhistory(wordId INTEGER PRIMARY KEY NOT NULL, queryTime INTEGER NOT NULL);
        CREATE TABLE wordfavorite(wordId INTEGER PRIMARY KEY NOT NULL, favoriteTime INTEGER NOT NULL);"""
    )
    conn.commit()


if __name__ == "__main__":
    conn = connect("./dict_mini.db")
    create_db(conn)
    import_csv_sqlite(conn)
    migrate(conn)
