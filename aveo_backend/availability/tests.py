from django.test import TestCase
from .models import Teacher, ValidSlot

# Create your tests here.


class TeacherTestCase(TestCase):
    def setUp(self):
        Teacher.objects.create(first_name="Testy", last_name="McTestFace")

    def test_entry_is_there(self):
        try:
            obj = Teacher.objects.get(first_name="Testy", last_name="McTestFace")
            assert True
        except Teacher.DoesNotExist as err:
            print(err)
            assert False
