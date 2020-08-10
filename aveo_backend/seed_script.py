import sqlite3
from pprint import pprint

sqlite_file = "db.sqlite3"


def create_connection(db_file):
    conn = None
    try:
        conn = sqlite3.connect(db_file)
    except BaseException as e:
        print(e)

    return conn


def remove_everything(conn):
    sql1 = "DELETE FROM availability_teacher"
    sql2 = "DELETE FROM availability_validslot"
    sql3 = "DELETE FROM availability_availableslot"

    cur = conn.cursor()

    cur.execute(sql1)
    cur.execute(sql2)
    cur.execute(sql3)

    conn.commit()
    return cur.lastrowid


def add_teachers(conn):
    cur = conn.cursor()
    teachers = [
        ["Nikola", "Tesla"],
        ["Lil", "Wayne"],
        ["Binod", "Binod"],
        ["Ghatot", "Kach"],
    ]
    for teacher in teachers:
        sql = "INSERT INTO availability_teacher (first_name, last_name) "
        sql += "VALUES ('{}', '{}')".format(teacher[0], teacher[1])
        cur.execute(sql)

    conn.commit()
    return cur.lastrowid


def add_valid_slots_weekdays(conn):
    cur = conn.cursor()
    days = list(range(0, 5))
    times = ["10:00", "11:00", "13:00", "15:00", "16:00", "19:00"]

    sql = "INSERT INTO availability_validslot (day, start_time) "
    sql += "VALUES "

    for day in days:
        for time in times:
            sql += "({},'{}'), ".format(day, time)
    sql = sql[:-2] + " ;"

    cur.execute(sql)
    conn.commit()
    return cur.lastrowid


def add_valid_slots_weekends(conn):
    cur = conn.cursor()
    days = list(range(5, 7))
    times = ["10:00", "11:00", "12:00", "13:00", "15:00", "16:00", "19:00", "20:00"]

    sql = "INSERT INTO availability_validslot (day, start_time) "
    sql += "VALUES "

    for day in days:
        for time in times:
            sql += "({},'{}'), ".format(day, time)
    sql = sql[:-2] + " ;"

    cur.execute(sql)
    conn.commit()
    return cur.lastrowid


def add_available_slots(conn):
    pass


if __name__ == "__main__":
    conn = create_connection(sqlite_file)
    remove_everything(conn)
    add_teachers(conn)
    add_valid_slots_weekdays(conn)
    add_valid_slots_weekends(conn)
