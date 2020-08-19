from django.urls import path
from . import views_generics

prefix = "ta2/api/"

urlpatterns = [
    path("{}teacher/".format(prefix), views_generics.CreateViewTeacher.as_view()),
    path("{}teacher/<int:pk>/".format(prefix), views_generics.DetailsViewTeacher.as_view()),
    path("{}validslot/".format(prefix), views_generics.CreateViewValidSlot.as_view()),
    path(
        "{}availableslot/".format(prefix),
        views_generics.CreateViewAvailableSlot.as_view(),
    ),
    path(
        "{}availableslot/tid/<int:teacher_id>/".format(prefix),
        views_generics.availableslot_teacher_id,
    ),
    path(
        "{}availableslot/tid/<int:teacher_id>/delete/".format(prefix),
        views_generics.availableslot_teacher_id_list,
    ),
    path(
        "{}availableslot/tid/<int:teacher_id>/date/<str:date>/".format(prefix),
        views_generics.availableslot_teacher_id_date,
    ),
    path(
        "{}availableslot/tid/<int:teacher_id>/m/<int:month>/y/<int:year>/".format(prefix),
        views_generics.availableslot_teacher_id_month_year,
    )
]
