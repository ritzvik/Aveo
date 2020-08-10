from django.contrib import admin

from .models import Teacher, ValidSlot, AvailableSlot

# Register your models here.

admin.site.register(Teacher)
admin.site.register(ValidSlot)
admin.site.register(AvailableSlot)
