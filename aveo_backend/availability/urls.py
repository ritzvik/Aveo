from django.urls import path
from . import views
from . import views_generics
from . import views_test

urlpatterns = [
    path("ta/api/teacher/", views.get_teachers_list, name="get-teacher_list"),
    path("ta/api/teacher/add/", views.add_teacher, name="add-teacher"),
    path("ta/api/teacher/<int:pk>/", views.get_teacher, name="get-teacher"),
    path("<int:pk>/", views.test_view, name="tes-view"),
]

prefix = "ta2/api/"

urlpatterns += [
    path("{}teacher/".format(prefix), views_generics.CreateView_Teacher.as_view()),
    path(
        "{}teacher/<int:pk>/".format(prefix),
        views_generics.DetailsView_Teacher.as_view(),
    ),
    path("{}validslot/".format(prefix), views_generics.CreateView_ValidSlot.as_view()),
    path(
        "{}validslot/<int:pk>".format(prefix),
        views_generics.DetailsView_ValidSlot.as_view(),
    ),
    path("{}validslot/day/<int:day>/".format(prefix), views_generics.validslot__day),
    path("{}validslot/date/<str:date>/".format(prefix), views_generics.validslot__date),
    path(
        "{}availableslot/".format(prefix),
        views_generics.CreateView_AvailableSlot.as_view(),
    ),
    path(
        "{}availableslot/<int:pk>/".format(prefix),
        views_generics.DetailsView_AvailableSlot.as_view(),
    ),
    path(
        "{}availableslot/tid/<int:teacher_id>/".format(prefix),
        views_generics.availableslot___teacher_id,
    ),
    path(
        "{}availableslot/tid/<int:teacher_id>/delete/".format(prefix),
        views_generics.availableslot___teacher_id_list,
    ),
    path(
        "{}availableslot/tid/<int:teacher_id>/day/<int:day>/".format(prefix),
        views_generics.availableslot___teacher_id__validslot_day,
    ),
    path(
        "{}availableslot/tid/<int:teacher_id>/day/<int:day>/<str:date>/".format(prefix),
        views_generics.availableslot___teacher_id__validslot_day__date,
    ),
    path(
        "{}availableslot/tid/<int:teacher_id>/date/<str:date>/".format(prefix),
        views_generics.availableslot___teacher_id__date,
    ),
    path(
        "{}availableslot/tid/<int:teacher_id>/m/<int:month>/y/<int:year>/".format(
            prefix
        ),
        views_generics.availableslot___teacher_id__month__year,
    ),
    path("{}test/".format(prefix), views_test.test,),
]
