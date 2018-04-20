package org.activityinfo.dev.client;

import com.google.gwt.user.client.Random;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.core.client.ValueProvider;
import com.sencha.gxt.data.shared.ListStore;
import com.sencha.gxt.widget.core.client.grid.ColumnConfig;
import com.sencha.gxt.widget.core.client.grid.ColumnModel;
import com.sencha.gxt.widget.core.client.grid.Grid;
import com.sencha.gxt.widget.core.client.grid.GridView;
import org.activityinfo.ui.client.base.tablegrid.GridAppearance;

import java.util.ArrayList;
import java.util.List;

public class GridPanel implements IsWidget {


    private static final String[] COLUMNS = new String[]{
            "1. Protection Code / رمز الحماية",
            "2. Case Status? / حالة القضية؟",
            "3. Other Actions? / اجراءات أخرى",
            "3a. Original Protection Code / رمز الحماية الاصلي",
            "4. Date of Birth / تاريخ الولادة",
            "4. Date of Birth / تاريخ الولادة",
            "5. Family Registration No / رقم تسجيل العائلة",
            "6. Ledger No (خاص بالفلسطينيين القادمين من سوريا) ",
            "7. Sex / الجنس",
            "8. Registered with UNRWA / مسجل مع الأونروا",
            "9. If registered with UNRWA, where? / اذا كان مسجل مع الأونروا, فأين تم التسجيل؟",
            "10. Nationality / الجنسية",
            "10a. If other, specify / اذا كان غير ذلك, حدد",
            "11. PRS? / فلسطيني قادم من سوريا ",
            "12. Ex Gazan? / فلسطيني قادم من غزة؟",
            "13. Date entered Jordan / تاريخ دخول الأردن",
            "13. Date entered Jordan / تاريخ دخول الأردن",
            "14. Civil Status / الحالة الاجتماعية",
            "15. Age of Refugee?/عمر اللاجئ",
            "16. Is refugee a minor? / هل اللاجئ قاصر",
            "16a. If so, does refugee presently have a parent or guardian? / اذا كان اللاجئ قاصر, هل لديه ولي أمر",
            "17. CASE IDENTIFICATION VIA / تم التعرف على القضية من خلال",
            "17a. If other, specify / اذا كان غير ذلك, حدد",
            "18. PROTECTION CASE: INTERNATIONAL PROTECTION  / قضية حماية دولية",
            "19. PROTECTION CASE: GBV العنف المبني على النوع الاجتماعي :",
            "20. PROTECTION CASE: CHILD PROTECTION (under 18) | حالة الحماية: حماية الأطفال/القصر (دون سن 18)",
            "21. PROTECTION CASE: BARRIER TO ACCESS TO SERVICES / حالة حماية: رفض تقديم الخدمات أو الحرمان من الخدمات",
            "21a. TYPE OF BARRIER TO ACCESS TO  SERVICES INCIDENT / عدم تقديم الخدمات",
            "22. Was anyone else affected by this incident? / هل أثرت هذه الواقعة على شخص آخر؟",
            "23. How many persons were affected? / عدد الاشخاص الآخرين المتأثرين؟",
            "24. REFUGEE PROFILING: Vulnerable Family Situation / تحديد معالم اللاجئ *وضع العائلة صعب",
            "25. Refugee Vulnerabilities / أوجه اللاجئ الضعف",
            "26. Camp / المخيم",
            "26a. Other / غير ذلك",
            "27. Area / المنطقة",
            "28. REFERRAL TO ANOTHER CASE WORKER REQUIRED? / إحالة إلى موظف أونروا آخر لمتابعة الحالة ",
            "29a. Protection Social Worker (PSW)  / باحثوا الحماية",
            "29b. Operational Support Officer (OSO)",
            "29c. GBV Focal Point (GBV FP) / GBV جهة اتصال العنف القائم على النوع الاجتماعي",
            "30. Who will manage this case? / مَن سيكون مسؤول عن هذه القضية؟",
            "31. Case affects? القضية تؤثر على؟",
            "31a. If Individual case what is the protection Code of the Family case if exists? / في حال القضية لفرد ما هو رمز الحماية للعائلة ان وجد",
            "32. Initial UNRWA Interviewer / موظف الأونروا الذي قام بالمقابلة الأولى",
            "32a. Interviewer Contact Phone / رقم تلفون الموظف",
            "33 . Refugee / Caregiver signed a Consent? اللاجئ يرغب بالاحالة",
            "34. Refugee does not wish to be referred (non-consent) /  اللاجئ لا يرغب بالاحالة (الموافقة المكتوبة غير متوفرة) ",
            "35. Refugee wishes to be referred (consent) / اللاجىء موافق على الاحالة",
            "36. Level of Urgency |  درجة الأولوية",
            "PARTNER / من الشخص الذي سيستلم القضيه؟ ",
            "START DATE / تاريخ فتح الملف ",
            "END DATE - Follow up date / تاريخ متابعه القضيه ",
            "Project",
            "Count of entries INTAKE",
            "2. Individual Registration No / رقم تسجيل الفرد",
            "5. OLD GBV CODE"
    };

    private static final String[][] VALUES = new String[][] {
            { "001-001-0289", "002-034-0934", "001-001-0444"},
            { "Opened / مفتوحة", "Closed / مغلقة", ""},
            { "Case Transferred / القضية تم نقلها", "Case reopened / اعادة فتح القضية", "", "", "", ""},
            { "" },
            { "1982-01-01", "", "", ""},
            { "1-02632529" },
            { "6-40325621" },
            { "Male / ذكر", "Female / أنثى" },
            { "Yes / نعم", "No / لا" },
            { "Jordan", "Syria", "", "" }
    };

    private final Grid<DummyModel> grid;

    private static class DummyModel {
        private int id;
        private String[] values = new String[COLUMNS.length];

        public DummyModel(int id) {
            this.id = id;
            for (int i = 0; i < COLUMNS.length; i++) {
                String[] valueSet = VALUES[i % VALUES.length];
                values[i] = valueSet[Random.nextInt(valueSet.length)];
            }
        }

        public String getId() {
            return "row" + id;
        }

    }

    private static class DummyValueProvider implements ValueProvider<DummyModel, String> {

        private final int columnIndex;

        public DummyValueProvider(int columnIndex) {
            this.columnIndex = columnIndex;
        }

        @Override
        public String getValue(DummyModel object) {
            return object.values[columnIndex];
        }

        @Override
        public void setValue(DummyModel object, String value) {
        }

        @Override
        public String getPath() {
            return "column" + columnIndex;
        }
    }

    public GridPanel() {

        List<ColumnConfig<DummyModel, ?>> columns = new ArrayList<>();

        for (int i = 0; i < COLUMNS.length; i++) {
            ColumnConfig<DummyModel, String> config = new ColumnConfig<>(new DummyValueProvider(i));
            config.setHeader(COLUMNS[i]);
            columns.add(config);
        }

        ColumnModel<DummyModel> columnModel = new ColumnModel<>(columns);

        ListStore<DummyModel> store = new ListStore<>(m -> m.getId());
        for (int i = 0; i < 100; i++) {
            store.add(new DummyModel(i));
        }
        GridView<DummyModel> gridView = new GridView<>(new GridAppearance());
        gridView.setTrackMouseOver(false);

        grid = new Grid<>(store, columnModel, gridView);
    }

    @Override
    public Widget asWidget() {
        return grid;
    }
}
