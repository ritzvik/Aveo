from django.shortcuts import render, redirect
from django.http import HttpResponse, Http404, JsonResponse
from django.views.decorators.csrf import csrf_exempt
from rest_framework.parsers import JSONParser
from rest_framework.decorators import api_view
from rest_framework.response import Response
from rest_framework import generics

from .serializers import TestSerializer
from .models import AvailableSlot


@api_view(["GET"])
def test(request):
    objs = AvailableSlot.objects.all()
    serializer = TestSerializer(objs, many=True)
    return Response(serializer.data)
