from django.shortcuts import render, redirect
from django.http import HttpResponse, Http404, JsonResponse
from django.views.decorators.csrf import csrf_exempt
from rest_framework.parsers import JSONParser
from rest_framework.decorators import api_view
from rest_framework.response import Response
from rest_framework import generics

from .serializers import (
    TeacherSerializer,
    AvailableSlotSerializer,
    ValidSlotSerializer,
    ValidSlotResultsetSerializer,
    TestSerializer,
)
from .models import Teacher, AvailableSlot, ValidSlot

from django.db.models import Prefetch


@api_view(["GET"])
def validslot__day(request, day):
    objs = ValidSlot.objects.filter(day=day)
    serializer = ValidSlotSerializer(objs, many=True)
    return Response(serializer.data)


@api_view(["GET"])
def availableslot___teacher_id(request, teacher_id):
    objs = AvailableSlot.objects.filter(teacher_id__id=teacher_id)
    serializer = AvailableSlotSerializer(objs, many=True)
    return Response(serializer.data)


@api_view(["GET"])
def availableslot___teacher_id__validslot_day(request, teacher_id, day):
    objs = AvailableSlot.objects.filter(
        teacher_id__id=teacher_id, validslot_id__day=day
    )
    serializer = AvailableSlotSerializer(objs, many=True)
    return Response(serializer.data)


@api_view(["GET"])
def availableslot___teacher_id__validslot_day__date(request, teacher_id, day, date):
    objs = ValidSlot.objects.filter(day=day).prefetch_related(
        Prefetch(
            "slot",
            queryset=AvailableSlot.objects.filter(teacher_id=teacher_id, date=date),
        )
    )
    serializer = ValidSlotResultsetSerializer(objs, many=True)
    return Response(serializer.data)


class CreateView_Teacher(generics.ListCreateAPIView):
    queryset = Teacher.objects.all()
    serializer_class = TeacherSerializer

    def perform_create(self, serializer):
        serializer.save()


class CreateView_ValidSlot(generics.ListCreateAPIView):
    queryset = ValidSlot.objects.all()
    serializer_class = ValidSlotSerializer

    def perform_create(self, serializer):
        serializer.save()


class CreateView_AvailableSlot(generics.ListCreateAPIView):
    queryset = AvailableSlot.objects.all()
    serializer_class = AvailableSlotSerializer

    def perform_create(self, serializer):
        serializer.save()


class DetailsView_Teacher(generics.RetrieveUpdateDestroyAPIView):
    queryset = Teacher.objects.all()
    serializer_class = TeacherSerializer


class DetailsView_ValidSlot(generics.RetrieveUpdateDestroyAPIView):
    queryset = ValidSlot.objects.all()
    serializer_class = ValidSlotSerializer


class DetailsView_AvailableSlot(generics.RetrieveUpdateDestroyAPIView):
    queryset = AvailableSlot.objects.all()
    serializer_class = AvailableSlotSerializer
