package com.gaokao.advisor.major.init;

import com.gaokao.advisor.major.entity.Major;
import com.gaokao.advisor.major.repository.MajorRepository;
import com.gaokao.advisor.school.entity.School;
import com.gaokao.advisor.school.repository.SchoolRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Component
@Order(2)
@RequiredArgsConstructor
public class MajorDataInitializer implements CommandLineRunner {

    private final MajorRepository majorRepository;
    private final SchoolRepository schoolRepository;

    private static final Map<String, List<String>> CATEGORY_RULES = new LinkedHashMap<>();

    static {
        CATEGORY_RULES.put("医学", List.of("医", "药"));
        CATEGORY_RULES.put("农学", List.of("农业", "林业", "水产", "海洋"));
        CATEGORY_RULES.put("教育学", List.of("师范", "体育"));
        CATEGORY_RULES.put("法学", List.of("政法", "警察", "公安", "民族"));
        CATEGORY_RULES.put("艺术学", List.of("艺术", "传媒", "音乐", "美术", "戏剧", "舞蹈"));
    }

    private static final int MAJORS_PER_SCHOOL = 5;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void run(String... args) {
        if (majorRepository.count() > 0) {
            log.info("Major data already exists, skipping initialization.");
            return;
        }

        List<School> allSchools = schoolRepository.findAll();
        if (allSchools.isEmpty()) {
            log.warn("No schools found, skipping major initialization.");
            return;
        }

        // 1. Create all standalone major entities first
        List<MajorTemplate> templates = buildTemplates();
        List<Major> allMajors = templates.stream()
                .map(tpl -> {
                    Major m = new Major();
                    m.setName(tpl.name());
                    m.setCode(tpl.code());
                    m.setCategory(tpl.category());
                    m.setDegree(tpl.degree());
                    m.setDuration("四年");
                    return m;
                })
                .toList();
        majorRepository.saveAll(allMajors);

        // 2. Reload to get persistent IDs, group by category
        Map<String, List<Major>> majorsByCategory = majorRepository.findAll().stream()
                .collect(Collectors.groupingBy(Major::getCategory));

        // 3. Assign up to MAJORS_PER_SCHOOL majors per school using round-robin for even distribution
        int totalAssignments = 0;
        int schoolIndex = 0;
        for (School school : allSchools) {
            List<Major> selected = selectMajorsForSchool(school, majorsByCategory, allMajors, schoolIndex);
            school.getMajors().addAll(selected);
            totalAssignments += selected.size();
            schoolIndex++;
        }
        schoolRepository.saveAll(allSchools);

        log.info("Initialized {} major templates, assigned {} school-major associations across {} schools.",
                allMajors.size(), totalAssignments, allSchools.size());
    }

    private List<Major> selectMajorsForSchool(School school, Map<String, List<Major>> byCategory,
                                                List<Major> allMajors, int schoolIndex) {
        String schoolName = school.getName();
        List<Major> result = new ArrayList<>();

        // 1. Pick up to 2 majors from specialty categories matching the school name
        for (var entry : CATEGORY_RULES.entrySet()) {
            boolean matches = entry.getValue().stream().anyMatch(schoolName::contains);
            if (matches) {
                List<Major> candidates = byCategory.get(entry.getKey());
                if (candidates != null) {
                    for (Major m : candidates) {
                        if (result.size() >= 2) break;
                        result.add(m);
                    }
                }
            }
            if (result.size() >= 2) break;
        }

        // 2. Fill remaining slots via round-robin across ALL majors for even distribution
        if (result.size() < MAJORS_PER_SCHOOL) {
            int start = (schoolIndex * MAJORS_PER_SCHOOL) % allMajors.size();
            for (int i = 0; i < allMajors.size() && result.size() < MAJORS_PER_SCHOOL; i++) {
                int idx = (start + i) % allMajors.size();
                Major m = allMajors.get(idx);
                if (!result.contains(m)) {
                    result.add(m);
                }
            }
        }

        return result;
    }

    private List<MajorTemplate> buildTemplates() {
        return List.of(
                // ===== 哲学 =====
                new MajorTemplate("哲学", "010101", "哲学", "哲学学士"),
                new MajorTemplate("逻辑学", "010102", "哲学", "哲学学士"),
                new MajorTemplate("宗教学", "010103", "哲学", "哲学学士"),

                // ===== 经济学 =====
                new MajorTemplate("经济学", "020101", "经济学", "经济学学士"),
                new MajorTemplate("经济统计学", "020102", "经济学", "经济学学士"),
                new MajorTemplate("财政学", "020201", "经济学", "经济学学士"),
                new MajorTemplate("税收学", "020202", "经济学", "经济学学士"),
                new MajorTemplate("金融学", "020301", "经济学", "经济学学士"),
                new MajorTemplate("金融工程", "020302", "经济学", "经济学学士"),
                new MajorTemplate("保险学", "020303", "经济学", "经济学学士"),
                new MajorTemplate("投资学", "020304", "经济学", "经济学学士"),
                new MajorTemplate("国际经济与贸易", "020401", "经济学", "经济学学士"),
                new MajorTemplate("贸易经济", "020402", "经济学", "经济学学士"),

                // ===== 法学 =====
                new MajorTemplate("法学", "030101", "法学", "法学学士"),
                new MajorTemplate("知识产权", "030102", "法学", "法学学士"),
                new MajorTemplate("政治学与行政学", "030201", "法学", "法学学士"),
                new MajorTemplate("国际政治", "030202", "法学", "法学学士"),
                new MajorTemplate("社会学", "030301", "法学", "法学学士"),
                new MajorTemplate("社会工作", "030302", "法学", "法学学士"),
                new MajorTemplate("民族学", "030401", "法学", "法学学士"),
                new MajorTemplate("思想政治教育", "030503", "法学", "法学学士"),
                new MajorTemplate("治安学", "030601", "法学", "法学学士"),
                new MajorTemplate("侦查学", "030602", "法学", "法学学士"),

                // ===== 教育学 =====
                new MajorTemplate("教育学", "040101", "教育学", "教育学学士"),
                new MajorTemplate("科学教育", "040102", "教育学", "教育学学士"),
                new MajorTemplate("人文教育", "040103", "教育学", "教育学学士"),
                new MajorTemplate("学前教育", "040106", "教育学", "教育学学士"),
                new MajorTemplate("小学教育", "040107", "教育学", "教育学学士"),
                new MajorTemplate("特殊教育", "040108", "教育学", "教育学学士"),
                new MajorTemplate("体育教育", "040201", "教育学", "教育学学士"),
                new MajorTemplate("运动训练", "040202", "教育学", "教育学学士"),
                new MajorTemplate("社会体育指导与管理", "040203", "教育学", "教育学学士"),

                // ===== 文学 =====
                new MajorTemplate("汉语言文学", "050101", "文学", "文学学士"),
                new MajorTemplate("汉语言", "050102", "文学", "文学学士"),
                new MajorTemplate("汉语国际教育", "050103", "文学", "文学学士"),
                new MajorTemplate("英语", "050201", "文学", "文学学士"),
                new MajorTemplate("俄语", "050202", "文学", "文学学士"),
                new MajorTemplate("德语", "050203", "文学", "文学学士"),
                new MajorTemplate("法语", "050204", "文学", "文学学士"),
                new MajorTemplate("西班牙语", "050205", "文学", "文学学士"),
                new MajorTemplate("日语", "050207", "文学", "文学学士"),
                new MajorTemplate("新闻学", "050301", "文学", "文学学士"),
                new MajorTemplate("广播电视学", "050302", "文学", "文学学士"),
                new MajorTemplate("广告学", "050303", "文学", "文学学士"),
                new MajorTemplate("传播学", "050304", "文学", "文学学士"),
                new MajorTemplate("编辑出版学", "050305", "文学", "文学学士"),
                new MajorTemplate("翻译", "050261", "文学", "文学学士"),
                new MajorTemplate("商务英语", "050262", "文学", "文学学士"),

                // ===== 历史学 =====
                new MajorTemplate("历史学", "060101", "历史学", "历史学学士"),
                new MajorTemplate("世界史", "060102", "历史学", "历史学学士"),
                new MajorTemplate("考古学", "060103", "历史学", "历史学学士"),
                new MajorTemplate("文物与博物馆学", "060104", "历史学", "历史学学士"),

                // ===== 理学 =====
                new MajorTemplate("数学与应用数学", "070101", "理学", "理学学士"),
                new MajorTemplate("信息与计算科学", "070102", "理学", "理学学士"),
                new MajorTemplate("物理学", "070201", "理学", "理学学士"),
                new MajorTemplate("应用物理学", "070202", "理学", "理学学士"),
                new MajorTemplate("化学", "070301", "理学", "理学学士"),
                new MajorTemplate("应用化学", "070302", "理学", "理学学士"),
                new MajorTemplate("天文学", "070401", "理学", "理学学士"),
                new MajorTemplate("地理科学", "070501", "理学", "理学学士"),
                new MajorTemplate("自然地理与资源环境", "070502", "理学", "理学学士"),
                new MajorTemplate("大气科学", "070601", "理学", "理学学士"),
                new MajorTemplate("应用气象学", "070602", "理学", "理学学士"),
                new MajorTemplate("海洋科学", "070701", "理学", "理学学士"),
                new MajorTemplate("地球物理学", "070801", "理学", "理学学士"),
                new MajorTemplate("地质学", "070901", "理学", "理学学士"),
                new MajorTemplate("生物科学", "071001", "理学", "理学学士"),
                new MajorTemplate("生物技术", "071002", "理学", "理学学士"),
                new MajorTemplate("心理学", "071101", "理学", "理学学士"),
                new MajorTemplate("应用心理学", "071102", "理学", "理学学士"),
                new MajorTemplate("统计学", "071201", "理学", "理学学士"),
                new MajorTemplate("应用统计学", "071202", "理学", "理学学士"),

                // ===== 工学 =====
                new MajorTemplate("工程力学", "080102", "工学", "工学学士"),
                new MajorTemplate("机械工程", "080201", "工学", "工学学士"),
                new MajorTemplate("机械设计制造及其自动化", "080202", "工学", "工学学士"),
                new MajorTemplate("车辆工程", "080207", "工学", "工学学士"),
                new MajorTemplate("测控技术与仪器", "080301", "工学", "工学学士"),
                new MajorTemplate("材料科学与工程", "080401", "工学", "工学学士"),
                new MajorTemplate("冶金工程", "080404", "工学", "工学学士"),
                new MajorTemplate("金属材料工程", "080405", "工学", "工学学士"),
                new MajorTemplate("高分子材料与工程", "080407", "工学", "工学学士"),
                new MajorTemplate("能源与动力工程", "080501", "工学", "工学学士"),
                new MajorTemplate("新能源科学与工程", "080503", "工学", "工学学士"),
                new MajorTemplate("电气工程及其自动化", "080601", "工学", "工学学士"),
                new MajorTemplate("智能电网信息工程", "080602", "工学", "工学学士"),
                new MajorTemplate("电子信息工程", "080701", "工学", "工学学士"),
                new MajorTemplate("电子科学与技术", "080702", "工学", "工学学士"),
                new MajorTemplate("通信工程", "080703", "工学", "工学学士"),
                new MajorTemplate("微电子科学与工程", "080704", "工学", "工学学士"),
                new MajorTemplate("光电信息科学与工程", "080705", "工学", "工学学士"),
                new MajorTemplate("信息工程", "080706", "工学", "工学学士"),
                new MajorTemplate("自动化", "080801", "工学", "工学学士"),
                new MajorTemplate("机器人工程", "080803", "工学", "工学学士"),
                new MajorTemplate("计算机科学与技术", "080901", "工学", "工学学士"),
                new MajorTemplate("软件工程", "080902", "工学", "工学学士"),
                new MajorTemplate("网络工程", "080903", "工学", "工学学士"),
                new MajorTemplate("信息安全", "080904", "工学", "工学学士"),
                new MajorTemplate("物联网工程", "080905", "工学", "工学学士"),
                new MajorTemplate("数字媒体技术", "080906", "工学", "工学学士"),
                new MajorTemplate("数据科学与大数据技术", "080910", "工学", "工学学士"),
                new MajorTemplate("人工智能", "080911", "工学", "工学学士"),
                new MajorTemplate("土木工程", "081001", "工学", "工学学士"),
                new MajorTemplate("给排水科学与工程", "081003", "工学", "工学学士"),
                new MajorTemplate("水利水电工程", "081101", "工学", "工学学士"),
                new MajorTemplate("水文与水资源工程", "081102", "工学", "工学学士"),
                new MajorTemplate("测绘工程", "081201", "工学", "工学学士"),
                new MajorTemplate("遥感科学与技术", "081202", "工学", "工学学士"),
                new MajorTemplate("化学工程与工艺", "081301", "工学", "工学学士"),
                new MajorTemplate("制药工程", "081302", "工学", "工学学士"),
                new MajorTemplate("地质工程", "081401", "工学", "工学学士"),
                new MajorTemplate("勘查技术与工程", "081402", "工学", "工学学士"),
                new MajorTemplate("采矿工程", "081501", "工学", "工学学士"),
                new MajorTemplate("石油工程", "081502", "工学", "工学学士"),
                new MajorTemplate("纺织工程", "081601", "工学", "工学学士"),
                new MajorTemplate("服装设计与工程", "081602", "工学", "工学学士"),
                new MajorTemplate("轻化工程", "081701", "工学", "工学学士"),
                new MajorTemplate("交通运输", "081801", "工学", "工学学士"),
                new MajorTemplate("交通工程", "081802", "工学", "工学学士"),
                new MajorTemplate("航海技术", "081803", "工学", "工学学士"),
                new MajorTemplate("飞行技术", "081804", "工学", "工学学士"),
                new MajorTemplate("船舶与海洋工程", "081901", "工学", "工学学士"),
                new MajorTemplate("航空航天工程", "082001", "工学", "工学学士"),
                new MajorTemplate("飞行器设计与工程", "082002", "工学", "工学学士"),
                new MajorTemplate("飞行器动力工程", "082004", "工学", "工学学士"),
                new MajorTemplate("核工程与核技术", "082201", "工学", "工学学士"),
                new MajorTemplate("农业工程", "082301", "工学", "工学学士"),
                new MajorTemplate("农业机械化及其自动化", "082302", "工学", "工学学士"),
                new MajorTemplate("林业工程", "082401", "工学", "工学学士"),
                new MajorTemplate("木材科学与工程", "082402", "工学", "工学学士"),
                new MajorTemplate("环境科学与工程", "082501", "工学", "工学学士"),
                new MajorTemplate("环境工程", "082502", "工学", "工学学士"),
                new MajorTemplate("环境科学", "082503", "工学", "工学学士"),
                new MajorTemplate("食品科学与工程", "082701", "工学", "工学学士"),
                new MajorTemplate("食品质量与安全", "082702", "工学", "工学学士"),
                new MajorTemplate("建筑学", "082801", "工学", "工学学士"),
                new MajorTemplate("城乡规划", "082802", "工学", "工学学士"),
                new MajorTemplate("风景园林", "082803", "工学", "工学学士"),
                new MajorTemplate("生物工程", "083001", "工学", "工学学士"),
                new MajorTemplate("生物制药", "083002", "工学", "工学学士"),
                new MajorTemplate("安全工程", "082901", "工学", "工学学士"),
                new MajorTemplate("刑事科学技术", "083101", "工学", "工学学士"),
                new MajorTemplate("消防工程", "083102", "工学", "工学学士"),

                // ===== 农学 =====
                new MajorTemplate("农学", "090101", "农学", "农学学士"),
                new MajorTemplate("园艺", "090102", "农学", "农学学士"),
                new MajorTemplate("植物保护", "090103", "农学", "农学学士"),
                new MajorTemplate("种子科学与工程", "090105", "农学", "农学学士"),
                new MajorTemplate("动物科学", "090301", "农学", "农学学士"),
                new MajorTemplate("动物医学", "090401", "农学", "农学学士"),
                new MajorTemplate("林学", "090501", "农学", "农学学士"),
                new MajorTemplate("园林", "090502", "农学", "农学学士"),
                new MajorTemplate("水产养殖学", "090601", "农学", "农学学士"),
                new MajorTemplate("草业科学", "090701", "农学", "农学学士"),

                // ===== 医学 =====
                new MajorTemplate("基础医学", "100101", "医学", "医学学士"),
                new MajorTemplate("临床医学", "100201", "医学", "医学学士"),
                new MajorTemplate("麻醉学", "100202", "医学", "医学学士"),
                new MajorTemplate("医学影像学", "100203", "医学", "医学学士"),
                new MajorTemplate("眼视光医学", "100204", "医学", "医学学士"),
                new MajorTemplate("精神医学", "100205", "医学", "医学学士"),
                new MajorTemplate("放射医学", "100206", "医学", "医学学士"),
                new MajorTemplate("口腔医学", "100301", "医学", "医学学士"),
                new MajorTemplate("预防医学", "100401", "医学", "医学学士"),
                new MajorTemplate("食品卫生与营养学", "100402", "医学", "理学学士"),
                new MajorTemplate("中医学", "100501", "医学", "医学学士"),
                new MajorTemplate("针灸推拿学", "100502", "医学", "医学学士"),
                new MajorTemplate("藏医学", "100503", "医学", "医学学士"),
                new MajorTemplate("中西医临床医学", "100601", "医学", "医学学士"),
                new MajorTemplate("药学", "100701", "医学", "理学学士"),
                new MajorTemplate("药物制剂", "100702", "医学", "理学学士"),
                new MajorTemplate("临床药学", "100703", "医学", "理学学士"),
                new MajorTemplate("中药学", "100801", "医学", "理学学士"),
                new MajorTemplate("中药制药", "100805", "医学", "理学学士"),
                new MajorTemplate("护理学", "101101", "医学", "理学学士"),
                new MajorTemplate("助产学", "101102", "医学", "理学学士"),
                new MajorTemplate("医学检验技术", "101001", "医学", "理学学士"),
                new MajorTemplate("医学实验技术", "101002", "医学", "理学学士"),
                new MajorTemplate("医学影像技术", "101003", "医学", "理学学士"),
                new MajorTemplate("眼视光学", "101004", "医学", "理学学士"),
                new MajorTemplate("康复治疗学", "101005", "医学", "理学学士"),
                new MajorTemplate("卫生检验与检疫", "101007", "医学", "理学学士"),

                // ===== 管理学 =====
                new MajorTemplate("管理科学", "120101", "管理学", "管理学学士"),
                new MajorTemplate("信息管理与信息系统", "120102", "管理学", "管理学学士"),
                new MajorTemplate("工程管理", "120103", "管理学", "管理学学士"),
                new MajorTemplate("房地产开发与管理", "120104", "管理学", "管理学学士"),
                new MajorTemplate("工商管理", "120201", "管理学", "管理学学士"),
                new MajorTemplate("市场营销", "120202", "管理学", "管理学学士"),
                new MajorTemplate("会计学", "120203", "管理学", "管理学学士"),
                new MajorTemplate("财务管理", "120204", "管理学", "管理学学士"),
                new MajorTemplate("国际商务", "120205", "管理学", "管理学学士"),
                new MajorTemplate("人力资源管理", "120206", "管理学", "管理学学士"),
                new MajorTemplate("审计学", "120207", "管理学", "管理学学士"),
                new MajorTemplate("资产评估", "120208", "管理学", "管理学学士"),
                new MajorTemplate("文化产业管理", "120210", "管理学", "管理学学士"),
                new MajorTemplate("农业经济管理", "120301", "管理学", "管理学学士"),
                new MajorTemplate("农村区域发展", "120302", "管理学", "管理学学士"),
                new MajorTemplate("公共事业管理", "120401", "管理学", "管理学学士"),
                new MajorTemplate("行政管理", "120402", "管理学", "管理学学士"),
                new MajorTemplate("劳动与社会保障", "120403", "管理学", "管理学学士"),
                new MajorTemplate("土地资源管理", "120404", "管理学", "管理学学士"),
                new MajorTemplate("城市管理", "120405", "管理学", "管理学学士"),
                new MajorTemplate("图书馆学", "120501", "管理学", "管理学学士"),
                new MajorTemplate("档案学", "120502", "管理学", "管理学学士"),
                new MajorTemplate("信息资源管理", "120503", "管理学", "管理学学士"),
                new MajorTemplate("物流管理", "120601", "管理学", "管理学学士"),
                new MajorTemplate("物流工程", "120602", "管理学", "管理学学士"),
                new MajorTemplate("电子商务", "120801", "管理学", "管理学学士"),
                new MajorTemplate("旅游管理", "120901", "管理学", "管理学学士"),
                new MajorTemplate("酒店管理", "120902", "管理学", "管理学学士"),
                new MajorTemplate("会展经济与管理", "120903", "管理学", "管理学学士"),

                // ===== 艺术学 =====
                new MajorTemplate("艺术史论", "130101", "艺术学", "艺术学学士"),
                new MajorTemplate("音乐表演", "130201", "艺术学", "艺术学学士"),
                new MajorTemplate("音乐学", "130202", "艺术学", "艺术学学士"),
                new MajorTemplate("作曲与作曲技术理论", "130203", "艺术学", "艺术学学士"),
                new MajorTemplate("舞蹈表演", "130204", "艺术学", "艺术学学士"),
                new MajorTemplate("舞蹈学", "130205", "艺术学", "艺术学学士"),
                new MajorTemplate("舞蹈编导", "130206", "艺术学", "艺术学学士"),
                new MajorTemplate("表演", "130301", "艺术学", "艺术学学士"),
                new MajorTemplate("戏剧学", "130302", "艺术学", "艺术学学士"),
                new MajorTemplate("电影学", "130303", "艺术学", "艺术学学士"),
                new MajorTemplate("广播电视编导", "130305", "艺术学", "艺术学学士"),
                new MajorTemplate("戏剧影视文学", "130304", "艺术学", "艺术学学士"),
                new MajorTemplate("戏剧影视导演", "130306", "艺术学", "艺术学学士"),
                new MajorTemplate("戏剧影视美术设计", "130307", "艺术学", "艺术学学士"),
                new MajorTemplate("录音艺术", "130308", "艺术学", "艺术学学士"),
                new MajorTemplate("播音与主持艺术", "130309", "艺术学", "艺术学学士"),
                new MajorTemplate("动画", "130310", "艺术学", "艺术学学士"),
                new MajorTemplate("美术学", "130401", "艺术学", "艺术学学士"),
                new MajorTemplate("绘画", "130402", "艺术学", "艺术学学士"),
                new MajorTemplate("雕塑", "130403", "艺术学", "艺术学学士"),
                new MajorTemplate("摄影", "130404", "艺术学", "艺术学学士"),
                new MajorTemplate("书法学", "130405", "艺术学", "艺术学学士"),
                new MajorTemplate("中国画", "130406", "艺术学", "艺术学学士"),
                new MajorTemplate("艺术设计学", "130501", "艺术学", "艺术学学士"),
                new MajorTemplate("视觉传达设计", "130502", "艺术学", "艺术学学士"),
                new MajorTemplate("环境设计", "130503", "艺术学", "艺术学学士"),
                new MajorTemplate("产品设计", "130504", "艺术学", "艺术学学士"),
                new MajorTemplate("服装与服饰设计", "130505", "艺术学", "艺术学学士"),
                new MajorTemplate("数字媒体艺术", "130508", "艺术学", "艺术学学士")
        );
    }

    private record MajorTemplate(String name, String code, String category, String degree) {}
}
