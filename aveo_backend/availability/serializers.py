from rest_framework import serializers
from .models import Teacher, ValidSlot, AvailableSlot


class TeacherSerializer(serializers.ModelSerializer):
    class Meta:
        model = Teacher
        fields = ("id", "first_name", "last_name")


class ValidSlotSerializer(serializers.ModelSerializer):
    class Meta:
        pass


class AvailableSlotSerializer(serializers.ModelSerializer):
    class Meta:
        pass
