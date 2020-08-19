from rest_framework import serializers
from .models import Teacher, ValidSlot, AvailableSlot


class TeacherSerializer(serializers.ModelSerializer):
    class Meta:
        model = Teacher
        fields = ("id", "first_name", "last_name")


class ValidSlotSerializer(serializers.ModelSerializer):
    class Meta:
        model = ValidSlot
        fields = "__all__"


class AvailableSlotSerializer(serializers.ModelSerializer):
    class Meta:
        model = AvailableSlot
        fields = "__all__"


class ValidSlotResultsetSerializer(serializers.ModelSerializer):
    slot = AvailableSlotSerializer(many=True)

    class Meta:
        model = ValidSlot
        fields = "__all__"
