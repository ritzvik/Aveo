from .models import Teacher, AvailableSlot, ValidSlot
from rest_framework.decorators import api_view
from rest_framework.response import Response


def run_seed():
    def add_teachers():
        first_names = ["Nikola", "Lil", "Binod"]
        last_names = ["Tesla", "Wayne", "Binod"]
        for fn, ln in zip(first_names, last_names):
            t = Teacher(first_name=fn, last_name=ln)
            t.save()

    def add_valid_slots_weekdays():
        days = list(range(0, 5))
        times = ["10:00", "11:00", "13:00", "15:00", "16:00", "19:00"]
        for day in days:
            for time in times:
                v = ValidSlot(day=day, start_time=time)
                v.save()

    def add_valid_slots_weekend():
        days = list(range(5, 7))
        times = ["10:00", "11:00", "12:00", "13:00", "15:00", "16:00", "19:00", "20:00"]
        for day in days:
            for time in times:
                v = ValidSlot(day=day, start_time=time)
                v.save()

    def clear_all():
        Teacher.objects.all().delete()
        ValidSlot.objects.all().delete()
        AvailableSlot.objects.all().delete()

    clear_all()
    add_teachers()
    add_valid_slots_weekdays()
    add_valid_slots_weekend()


@api_view(["GET"])
def seed(request):
    run_seed()
    return Response({"response": "done"})
