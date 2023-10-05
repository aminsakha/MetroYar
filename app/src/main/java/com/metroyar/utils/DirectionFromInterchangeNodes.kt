package com.metroyar.utils

fun getDirectionFromInterchangeStations(currId: Int, nextId: Int): String {
    when (findStationObjectFromItsId(currId).stationName) {
        "شهید بهشتی" -> {
            when (findStationObjectFromItsId(nextId).stationName) {
                "سهروردی" -> return "به سمت قائم"
                "شهید مفتح" -> return "به سمت کهریزک"
                "میرزای شیرازی" -> return "به سمت آزادگان"
                "مصلی" -> return "به سمت تجریش"
            }
        }

        "دروازه دولت" -> {
            when (findStationObjectFromItsId(nextId).stationName) {
                "دروازه شمیران" -> return "به سمت شهید کلاهدوز"
                "سعدی" -> return "به سمت کهریزک"
                "فردوسی" -> return "به سمت علامه جعفری"
                "طالقانی" -> return "به سمت تجریش"
            }
        }

        "امام خمینی" -> {
            when (findStationObjectFromItsId(nextId).stationName) {
                "ملت" -> return "به سمت فرهنگسرا"
                "پانزده خرداد" -> return "به سمت کهریزک"
                "حسن آباد" -> return "به سمت تهران (صادقیه)"
                "سعدی" -> return "به سمت تجریش"
            }
        }

        "میدان محمدیه" -> {
            when (findStationObjectFromItsId(nextId).stationName) {
                "مهدیه" -> return "به سمت میدان کتاب"
                "مولوی" -> return "به سمت بسیج"
                "شوش" -> return "به سمت کهریزک"
                "خیام" -> return "به سمت تجریش"
            }
        }

        "امام حسین" -> {
            when (findStationObjectFromItsId(nextId).stationName) {
                "میدان شهدا" -> return "به سمت دولت آباد"
                "دروازه شمیران" -> return "به سمت تهران (صادقیه)"
                "شهید مدنی" -> return "به سمت فرهنگسرا"
                "هفت تیر" -> return "به سمت شهید ستاری"
            }
        }

        "دروازه شمیران" -> {
            when (findStationObjectFromItsId(nextId).stationName) {
                "دروازه دولت" -> return "به سمت علامه جعفری"
                "بهارستان" -> return "به سمت تهران (صادقیه)"
                "امام حسین" -> return "به سمت فرهنگسرا"
                "میدان شهدا" -> return "به سمت شهید کلاهدوز"
            }
        }

        "شهید نواب صفوی" -> {
            when (findStationObjectFromItsId(nextId).stationName) {
                "شادمان" -> return "به سمت تهران (صادقیه)"
                "میدان حر" -> return "به سمت فرهنگسرا"
                "رودکی" -> return "به سمت بسیج"
                "توحید" -> return "به سمت میدان کتاب"
            }
        }

        "شادمان" -> {
            when (findStationObjectFromItsId(nextId).stationName) {
                "دانشگاه شریف" -> return "به سمت تهران (صادقیه)"
                "شهید نواب صفوی" -> return "به سمت فرهنگسرا"
                "دکتر حبیب الله" -> return "به سمت علامه جعفری"
                "توحید" -> return "به سمت شهید کلاهدوز"
            }
        }

        "تهران (صادقیه)" -> {
            when (findStationObjectFromItsId(nextId).stationName) {
                "ارم سبز" -> return "شهید قاسم سلیمانی"
                "طرشت" -> return "به سمت فرهنگسرا"
            }
        }

        "تئاتر شهر" -> {
            when (findStationObjectFromItsId(nextId).stationName) {
                "منیریه" -> return "به سمت آزادگان"
                "میدان انقلاب" -> return "به سمت علامه جعفری"
                "فردوسی" -> return "به سمت شهید کلاهدوز"
                "میدان ولیعصر" -> return "به سمت قائم"
            }
        }

        "مهدیه" -> {
            when (findStationObjectFromItsId(nextId).stationName) {
                "هلال احمر" -> return "به سمت میدان کتاب"
                "میدان محمدیه" -> return "به سمت بسیج"
                "راه آهن" -> return "به سمت آزادگان"
                "منیریه" -> return "به سمت قائم"
            }
        }

        "میدان شهدا" -> {
            when (findStationObjectFromItsId(nextId).stationName) {
                "امیر کبیر" -> return "به سمت دولت آباد"
                "امام حسین" -> return "به سمت شهید ستاری"
                "دروازه شمیران" -> return "به سمت علامه جعفری"
                "ابن سینا" -> return "به سمت شهید کلاهدوز"
            }
        }

        "توحید" -> {
            when (findStationObjectFromItsId(nextId).stationName) {
                "مدافعان سلامت" -> return "به سمت میدان کتاب"
                "شهید نواب صفوی" -> return "به سمت بسیج"
                "شادمان" -> return "به سمت علامه جعفری"
                "میدان انقلاب" -> return "به سمت شهید کلاهدوز"
            }
        }

        "ارم سبز" -> {
            when (findStationObjectFromItsId(nextId).stationName) {
                "ورزشگاه آزادی" -> return "شهید قاسم سلیمانی"
                "تهران (صادقیه)" -> return "به سمت تهران (صادقیه)"
                "شهرک اکباتان" -> return "به سمت شهید کلاهدوز"
                "علامه جعفری" -> return "به سمت علامه جعفری"
            }
        }

        "دانشگاه تربیت مدرس" -> {
            when (findStationObjectFromItsId(nextId).stationName) {
                "شهرک آزمایش" -> return "به سمت شهید ستاری"
                "مدافعان سلامت" -> return "به سمت بسیج"
                "بوستان گفتگو" -> return "به سمت میدان کتاب"
                "کارگر" -> return "به سمت دولت آباد"
            }
        }

        "میدان ولیعصر" -> {
            when (findStationObjectFromItsId(nextId).stationName) {
                "میدان جهاد" -> return "به سمت قائم"
                "بوستان لاله" -> return "به سمت شهید ستاری"
                "تئاتر شهر" -> return "به سمت آزادگان"
                "شهید نجات اللهی" -> return "به سمت دولت آباد"
            }
        }

        "هفت تیر" -> {
            when (findStationObjectFromItsId(nextId).stationName) {
                "طالقانی" -> return "به سمت کهریزک"
                "امام حسین" -> return "به سمت دولت آباد"
                "شهید مفتح" -> return "به سمت تجریش"
                "شهید نجات اللهی" -> return "به سمت شهید ستاری"
            }
        }

        "شاهد - باقرشهر" -> {
            when (findStationObjectFromItsId(nextId).stationName) {
                "نمایشگاه شهر آفتاب" -> return "به سمت فرودگاه امام خمینی"
                "حرم مطهر" -> return "به سمت کهریزک"
                "پالایشگاه" -> return "به سمت تجریش"
            }
        }

        "بیمه" -> {
            when (findStationObjectFromItsId(nextId).stationName) {
                "شهرک اکباتان" -> return "به سمت علامه جعفری"
                "پایانه ۱ و ۲ فرودگاه مهرآباد" -> return "به سمت پایانه ۴ و ۶ فرودگاه مهرآباد"
                "میدان آزادی" -> return "به سمت شهید کلاهدوز"
            }
        }
    }
    return "خطا"
}