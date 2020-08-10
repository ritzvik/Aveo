from django.shortcuts import render, redirect
from django.http import HttpResponse, Http404, JsonResponse
from django.views.decorators.csrf import csrf_exempt
from rest_framework.parsers import JSONParser
from rest_framework.decorators import api_view
from rest_framework.response import Response
from rest_framework import generics

from .serializers import TeacherSerializer, AvailableSlotSerializer, ValidSlotSerializer
from .models import Teacher, AvailableSlot, ValidSlot


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


class DetailsView_Teacher(generics.RetrieveUpdateDestroyAPIView):
    queryset = Teacher.objects.all()
    serializer_class = TeacherSerializer


class DetailsView_ValidSlot(generics.RetrieveUpdateDestroyAPIView):
    queryset = ValidSlot.objects.all()
    serializer_class = ValidSlotSerializer
