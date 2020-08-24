from datetime import datetime
from django.http import JsonResponse
from rest_framework.decorators import api_view
from rest_framework.response import Response
from rest_framework import generics
from rest_framework import status

from .serializers import (
    TeacherSerializer,
    AvailableSlotSerializer,
    ValidSlotSerializer,
    ValidSlotResultsetSerializer,
)
from .models import Teacher, AvailableSlot, ValidSlot

from django.db.models import Prefetch
from django.core.cache import cache


def day_from_date(datestring: str):
    date_obj = datetime.strptime(datestring, "%Y-%m-%d")
    return date_obj.weekday()


@api_view(["GET"])
def availableslot_teacher_id(request, teacher_id):
    cache_key = "availableslot_tid_{}".format(teacher_id)
    data = cache.get(cache_key)
    if data is None:
        objs = AvailableSlot.objects.filter(teacher_id__id=teacher_id)
        serializer = AvailableSlotSerializer(objs, many=True)
        data = serializer.data
        cache.set(cache_key, data)
    return Response(data)


@api_view(["GET"])
def availableslot_teacher_id_date(request, teacher_id, date):
    day = day_from_date(date)
    objs = ValidSlot.objects.filter(day=day).prefetch_related(
        Prefetch(
            "slot",
            queryset=AvailableSlot.objects.filter(teacher_id=teacher_id, date=date),
        )
    )
    serializer = ValidSlotResultsetSerializer(objs, many=True)
    return Response(serializer.data)


@api_view(["GET"])
def availableslot_teacher_id_month_year(request, teacher_id, month, year):
    def _enddate(month: int, year: int):
        enddate_lists = [31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31]
        if year % 4 == 0:
            enddate_lists[1] = 29
        return enddate_lists[month - 1]

    month = str(month).zfill(2)
    year = str(year).zfill(4)
    startdate = "{}-{}-01".format(year, month)
    enddate = "{}-{}-{}".format(year, month, str(_enddate(int(month), int(year))))
    objs = AvailableSlot.objects.filter(
        teacher_id__id=teacher_id, date__gte=startdate, date__lte=enddate
    )
    serializer = AvailableSlotSerializer(objs, many=True)
    return Response(serializer.data)


@api_view(["DELETE", "GET"])
def availableslot_teacher_id_list(request, teacher_id):
    objs = AvailableSlot.objects.filter(teacher_id=teacher_id, id__in=request.data)
    if len(objs) < len(request.data):
        return JsonResponse(
            {"message": "Slots does'nt exist"}, status=status.HTTP_404_NOT_FOUND
        )

    if request.method == "DELETE":
        objs.delete()
        cache_key = "availableslot_tid_{}".format(teacher_id)
        cache.delete(cache_key)
        return Response(status=status.HTTP_204_NO_CONTENT)
    elif request.method == "GET":
        if len(request.data) < 1:
            objs = AvailableSlot.objects.filter()
        serializer = AvailableSlotSerializer(objs, many=True)
        return Response(serializer.data)


class CreateViewTeacher(generics.ListCreateAPIView):
    queryset = Teacher.objects.all()
    serializer_class = TeacherSerializer

    def perform_create(self, serializer):
        serializer.save()


class CreateViewValidSlot(generics.ListCreateAPIView):
    queryset = ValidSlot.objects.all()
    serializer_class = ValidSlotSerializer

    def perform_create(self, serializer):
        serializer.save()


class CreateViewAvailableSlot(generics.ListCreateAPIView):
    queryset = AvailableSlot.objects.all()
    serializer_class = AvailableSlotSerializer

    def post(self, request, *args, **kwargs):
        data = request.data
        try:
            if isinstance(data, list):
                tIDs = list()
                for d in data:
                    tIDs.append(d["teacher_id"])
                tIDs = list(set(tIDs))
                for tID in tIDs:
                    cache_key = "availableslot_tid_{}".format(tID)
                    cache.delete(cache_key)
            else:
                tID = data["teacher_id"]
                cache_key = "availableslot_tid_{}".format(tID)
                cache.delete(cache_key)
        except:
            pass
        return super().post(request, *args, **kwargs)

    def perform_create(self, serializer):
        serializer.save()

    def get_serializer(self, *args, **kwargs):
        if isinstance(kwargs.get("data", {}), list):
            kwargs["many"] = True
        return super(CreateViewAvailableSlot, self).get_serializer(*args, **kwargs)


class DetailsViewTeacher(generics.RetrieveUpdateDestroyAPIView):
    queryset = Teacher.objects.all()
    serializer_class = TeacherSerializer
