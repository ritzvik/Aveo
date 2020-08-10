from django.shortcuts import render
from django.http import JsonResponse
from rest_framework.decorators import api_view
from rest_framework.response import Response
from .serializers import TeacherSerializer, AvailableSlotSerializer
from .models import Teacher, AvailableSlot, ValidSlot


# APIs For teachers table
@api_view(["GET"])
def get_teachers_list(request):
    teacher = Teacher.objects.all()
    serializer = TeacherSerializer(teacher, many=True)
    return Response(serializer.data)


@api_view(["POST"])
def add_teacher(request):
    serializer = TeacherSerializer(data=request.data)
    if serializer.is_valid():
        serializer.save()
    return Response(serializer.data)


@api_view(["GET"])
def get_teacher(request, pk):
    teacher = Teacher.objects.get(id=pk)
    serializer = TeacherSerializer(teacher, many=False)
    return Response(serializer.data)


@api_view(["GET"])
def test_view(request, pk):
    teacher = Teacher.objects.get(id=pk)
    slots = teacher.availableslot_set.all()
    serializer = AvailableSlotSerializer(slots, many=True)
    return Response(serializer.data)

