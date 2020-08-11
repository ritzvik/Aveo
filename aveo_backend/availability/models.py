from django.db import models


class Teacher(models.Model):
    id = models.AutoField(primary_key=True)
    first_name = models.TextField()
    last_name = models.TextField()


class ValidSlot(models.Model):
    id = models.AutoField(primary_key=True)
    day = models.IntegerField()  # 0 for monday, 1 for tuesday .... 6 for sunday
    start_time = models.TimeField()  # slots are assumed to be of 1 hour, so no end_time


class AvailableSlot(models.Model):
    id = models.AutoField(primary_key=True)
    teacher_id = models.ForeignKey("Teacher", related_name='teacher', on_delete=models.SET_NULL, null=True)
    validslot_id = models.ForeignKey("ValidSlot", related_name='slot', on_delete=models.SET_NULL, null=True)
    date = models.DateField()
    status = models.IntegerField()  # 0 for cancelled, 1 for available, 2 for confirmed
