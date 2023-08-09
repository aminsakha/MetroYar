package com.metroyar.utils

fun getDirectionFromInterchangeStations(currId: Int, nextId: Int): String {
    when (findStationObjectFromItsId(currId).name) {
        "شهید بهشتی" -> {
            when (findStationObjectFromItsId(nextId).name) {
                "سهروردی" -> return "به سمت قائم"
                "شهید مفتح" -> return "به سمت کهریزک"
                "میرزای شیرازی" -> return "به سمت آزادگان"
                "مصلی امام خمینی" -> return "به سمت تجریش"
            }
        }

        "دروازه دولت" -> {
            when (findStationObjectFromItsId(nextId).name) {
                "دروازه شمیران" -> return "به سمت شهید کلاهدوز"
                "سعدی" -> return "به سمت کهریزک"
                "فردوسی" -> return "به سمت ارم سبز"
                "طالقانی" -> return "به سمت تجریش"
            }
        }

        "امام خمینی" -> {
            when (findStationObjectFromItsId(nextId).name) {
                "ملت" -> return "به سمت فرهنگسرا"
                "پانزده خرداد" -> return "به سمت کهریزک"
                "حسن آباد" -> return "به سمت تهران (صادقیه)"
                "سعدی" -> return "به سمت تجریش"
            }
        }

        "میدان محمدیه" -> {
            when (findStationObjectFromItsId(nextId).name) {
                "مهدیه" -> return "به سمت میدان صنعت"
                "مولوی" -> return "به سمت بسیج"
                "شوش" -> return "به سمت کهریزک"
                "خیام" -> return "به سمت تجریش"
            }
        }

        "امام حسین" -> {
            when (findStationObjectFromItsId(nextId).name) {
                "میدان شهدا" -> return "به سمت دولت آباد"
                "دروازه شمیران" -> return "به سمت تهران (صادقیه)"
                "شهید مدنی" -> return "به سمت فرهنگسرا"
                "شهدای هفتم تیر" -> return "به سمت شهید ستاری"
            }
        }

        "دروازه شمیران" -> {
            when (findStationObjectFromItsId(nextId).name) {
                "دروازه دولت" -> return "به سمت ارم سبز"
                "بهارستان" -> return "به سمت تهران (صادقیه)"
                "امام حسین" -> return "به سمت فرهنگسرا"
                "میدان شهدا" -> return "به سمت شهید کلاهدوز"
            }
        }

        "شهید نواب صفوی" -> {
            when (findStationObjectFromItsId(nextId).name) {
                "شادمان" -> return "به سمت تهران (صادقیه)"
                "میدان حر" -> return "به سمت فرهنگسرا"
                "رودکی" -> return "به سمت بسیج"
                "توحید" -> return "به سمت میدان صنعت"
            }
        }

        "شادمان" -> {
            when (findStationObjectFromItsId(nextId).name) {
                "دانشگاه شریف" -> return "به سمت تهران (صادقیه)"
                "شهید نواب صفوی" -> return "به سمت فرهنگسرا"
                "دکتر حبیب الله" -> return "به سمت ارم سبز"
                "توحید" -> return "به سمت شهید کلاهدوز"
            }
        }

        "تهران (صادقیه)" -> {
            when (findStationObjectFromItsId(nextId).name) {
                "ارم سبز" -> return "به سمت شهید سپهبد قاسم سلیمانی"
                "طرشت" -> return "به سمت فرهنگسرا"
            }
        }

        "تئاتر شهر" -> {
            when (findStationObjectFromItsId(nextId).name) {
                "منیریه" -> return "به سمت آزادگان"
                "میدان انقلاب اسلامی" -> return "به سمت ارم سبز"
                "فردوسی" -> return "به سمت شهید کلاهدوز"
                "میدان حضرت ولی‌عصر" -> return "به سمت قائم"
            }
        }

        "مهدیه" -> {
            when (findStationObjectFromItsId(nextId).name) {
                "هلال احمر" -> return "به سمت میدان صنعت"
                "میدان محمدیه" -> return "به سمت بسیج"
                "راه آهن" -> return "به سمت آزادگان"
                "منیریه" -> return "به سمت قائم"
            }
        }

        "میدان شهدا" -> {
            when (findStationObjectFromItsId(nextId).name) {
                "امیرکبیر" -> return "به سمت دولت آباد"
                "امام حسین" -> return "به سمت شهید ستاری"
                "دروازه شمیران" -> return "به سمت ارم سبز"
                "ابن سینا" -> return "به سمت شهید کلاهدوز"
            }
        }

        "توحید" -> {
            when (findStationObjectFromItsId(nextId).name) {
                "مدافعان سلامت" -> return "به سمت میدان صنعت"
                "شهید نواب صفوی" -> return "به سمت بسیج"
                "شادمان" -> return "به سمت ارم سبز"
                "میدان انقلاب اسلامی" -> return "به سمت شهید کلاهدوز"
            }
        }

        "ارم سبز" -> {
            when (findStationObjectFromItsId(nextId).name) {
                "ورزشگاه آزادی" -> return "به سمت شهید سپهبد قاسم سلیمانی"
                "تهران (صادقیه)" -> return "به سمت تهران (صادقیه)"
                "شهرک اکباتان" -> return "به سمت شهید کلاهدوز"
            }
        }

        "دانشگاه تربیت مدرس" -> {
            when (findStationObjectFromItsId(nextId).name) {
                "شهرک آزمایش" -> return "به سمت شهید ستاری"
                "مدافعان سلامت" -> return "به سمت بسیج"
                "بوستان گفتگو" -> return "به سمت میدان صنعت"
                "کارگر" -> return "به سمت دولت آباد"
            }
        }

        "میدان حضرت ولی‌عصر" -> {
            when (findStationObjectFromItsId(nextId).name) {
                "میدان جهاد" -> return "به سمت قائم"
                "بوستان لاله" -> return "به سمت شهید ستاری"
                "تئاتر شهر" -> return "به سمت‌آزادگان"
                "شهید نجات اللهی (افتتاح نشده)" -> return "به سمت دولت آباد"
            }
        }

        "شهدای هفتم تیر" -> {
            when (findStationObjectFromItsId(nextId).name) {
                "طالقانی" -> return "به سمت کهریزک"
                "امام حسین" -> return "به سمت دولت آباد"
                "شهید مفتح" -> return "به سمت تجریش"
                "شهید نجات اللهی (افتتاح نشده)" -> return "به سمت شهید ستاری"
            }
        }
    }
    return "خطا"
}