package com.indian.calendar

object LanguageMapper {
    // પારસી રોજ (Parsi Roz) - ૩૦ દિવસના નામ
    val parsiRoz = mapOf(
        "gu" to arrayOf("હોર્મઝદ", "બહમન", "અર્દિબહેશ્ત", "શહેરેવર", "અસ્પંદારમદ", "ખોરદાદ", "અમરદાદ", "દેપાદિન", "આદર", "આવા", "ખોરશેદ", "મોહોર", "તીર", "ગોશ", "દેપમહેર", "મહેર", "સ્રોશ", "રશ્ને", "ફરવરદિન", "બહેરામ", "રામ", "ગોવાદ", "દેપદિન", "દીન", "અશીસ્વંગ", "અષ્ટાદ", "આસમાન", "ઝમ્યાદ", "માહરાસ્પંદ", "અનેરાન"),
        "en" to arrayOf("Hormazd", "Bahman", "Ardibehesht", "Shahrevar", "Aspandarmad", "Khordad", "Amardad", "Depadin", "Adar", "Ava", "Khorshed", "Mohor", "Tir", "Gosh", "Depmeher", "Meher", "Srosh", "Rashne", "Farvardin", "Behram", "Ram", "Govad", "Depdin", "Din", "Ashisvang", "Ashtad", "Asman", "Zamyad", "Mahraspand", "Aneran")
    )

    // ઇસ્લામિક મહિનાના નામ
    val islamicMonths = mapOf(
        "gu" to arrayOf("મોહરમ", "સફર", "રબીઉલ અવ્વલ", "રબીઉલ આખર", "જમાદિલ અવ્વલ", "જમાદિલ આખર", "રજબ", "શાબાન", "રમઝાન", "શવ્વાલ", "ઝિલકદ", "ઝિલહિજ"),
        "en" to arrayOf("Muharram", "Safar", "Rabi' al-awwal", "Rabi' al-thani", "Jumada al-ula", "Jumada al-akhira", "Rajab", "Sha'ban", "Ramadan", "Shawwal", "Dhu al-Qi'dah", "Dhu al-Hijjah")
    )

    // શક સંવત (Saka/Indian Civil)
    val sakaMonths = mapOf(
        "gu" to arrayOf("ચૈત્ર", "વૈશાખ", "જ્યેષ્ઠ", "આષાઢ", "શ્રાવણ", "ભાદ્રપદ", "આશ્વિન", "કાર્તિક", "માર્ગશીર્ષ", "પૌષ", "માઘ", "ફાલ્ગુન"),
        "en" to arrayOf("Chaitra", "Vaisakha", "Jyaistha", "Asadha", "Sravana", "Bhadra", "Asvina", "Kartika", "Agrahayana", "Pausa", "Magha", "Phalguna")
    )

    // અંકોનું ગુજરાતી રૂપાંતર
    fun toGujaratiDigits(input: String): String {
        val en = arrayOf("0", "1", "2", "3", "4", "5", "6", "7", "8", "9")
        val gu = arrayOf("૦", "૧", "૨", "૩", "૪", "૫", "૬", "૭", "૮", "૯")
        var result = input
        for (i in 0..9) result = result.replace(en[i], gu[i])
        return result
    }
}

