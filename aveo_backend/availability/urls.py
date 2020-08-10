from django.urls import path
from . import views
from . import views_generics

urlpatterns = [
    path("ta/api/teacher/", views.get_teachers_list, name="get-teacher_list"),
    path("ta/api/teacher/add/", views.add_teacher, name="add-teacher"),
    path("ta/api/teacher/<int:pk>/", views.get_teacher, name="get-teacher"),
    path("<int:pk>/", views.test_view, name='tes-view')
]


urlpatterns += [
    path("try/api/teacher/", views_generics.CreateView_Teacher.as_view()),
    path("try/api/teacher/<int:pk>/", views_generics.DetailsView_Teacher.as_view()),
    path("try/api/validslot/",views_generics.CreateView_ValidSlot.as_view()),
    path("try/api/validslot/<int:pk>", views_generics.DetailsView_ValidSlot.as_view()),
]
