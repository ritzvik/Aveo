from django.urls import path
from . import views

urlpatterns = [
    path("ta/api/teacher/", views.get_teachers_list, name="get-teacher_list"),
    path("ta/api/teacher/add/", views.add_teacher, name="add-teacher"),
    path("ta/api/teacher/<int:pk>/", views.get_teacher, name="get-teacher"),
]