package cn.wanfeng.sp.test;


import cn.wanfeng.sp.util.InputStreamUtils;
import cn.wanfeng.sp.util.LogUtil;
import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.write.metadata.WriteSheet;
import com.alibaba.fastjson.JSON;
import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;
import net.minidev.json.JSONArray;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;

/**
 * @date: 2025-01-09 21:54
 * @author: luozh.wanfeng
 * @description:
 * @since:
 */
public class NoApplicationTest {

    private static final String TEMPLATE_LOCAL_PATH = "D:\\amberdata\\projects\\ermsapi\\erms-core\\src\\main\\resources\\file\\档案室年报统计模版.xls";

    @Test
    public void test(){
        String randomDataJson = "{\n" +
                "    \"field4382978544695\": 12043,\n" +
                "    \"field9075678543431\": \"0\",\n" +
                "    \"field3649978540632\": \"1\",\n" +
                "    \"field8213778542114\": \"0\",\n" +
                "    \"field5748878538036\": \"1\",\n" +
                "    \"field5598478539417\": \"0\",\n" +
                "    \"field8017778536854\": \"0\",\n" +
                "    \"field4944778535619\": \"0\",\n" +
                "    \"field8789978530856\": \"1\",\n" +
                "    \"field8328778533454\": \"1\",\n" +
                "    \"field3161578532174\": \"0\",\n" +
                "    \"field4609978529441\": \"0\",\n" +
                "    \"field5050278527526\": \"1\",\n" +
                "    \"field9284878709978\": \"1\",\n" +
                "    \"field1274278711378\": \"0\",\n" +
                "    \"field4375578701885\": \"1\",\n" +
                "    \"field9521978707515\": \"0\",\n" +
                "    \"field8436078706113\": \"0\",\n" +
                "    \"field2135478704798\": \"4\",\n" +
                "    \"field7489178703383\": \"0\",\n" +
                "    \"field4159078699535\": \"1\",\n" +
                "    \"field8680378807013\": \"1\",\n" +
                "    \"field9501878804399\": \"0\",\n" +
                "    \"field4386165146790\": \"4\",\n" +
                "    \"field1516565153234\": \"0\",\n" +
                "    \"field9955765150037\": \"0\",\n" +
                "    \"field3825966228931\": \"0\",\n" +
                "    \"field5814466232194\": \"0\",\n" +
                "    \"field6367566472166\": \"0\",\n" +
                "    \"field7864166469753\": \"0\",\n" +
                "    \"field5978666474298\": \"0\",\n" +
                "    \"field1517766266250\": \"0\",\n" +
                "    \"field7381467020828\": \"0\",\n" +
                "    \"field9691378000498\": \"0\",\n" +
                "    \"field3508178002613\": \"0\",\n" +
                "    \"field2313777993834\": \"0\",\n" +
                "    \"field9150167148706\": \"0\",\n" +
                "    \"field8396367249508\": \"0\",\n" +
                "    \"field8250767626829\": \"0\",\n" +
                "    \"field3067567647912\": \"0\",\n" +
                "    \"field6912467656557\": \"0\",\n" +
                "    \"field2535667657822\": \"0\",\n" +
                "    \"field4244267652742\": \"0\",\n" +
                "    \"field2807467655355\": \"0\",\n" +
                "    \"field4874467654107\": \"0\",\n" +
                "    \"field5189267651512\": \"0\",\n" +
                "    \"field4670467646084\": \"0\",\n" +
                "    \"field1984467649328\": \"0\",\n" +
                "    \"field6459467629228\": \"0\",\n" +
                "    \"field6823368207734\": \"0\",\n" +
                "    \"field4052068214445\": \"0\",\n" +
                "    \"field3037968212946\": \"0\",\n" +
                "    \"field5166168205952\": \"0\",\n" +
                "    \"field5157468211597\": \"0\",\n" +
                "    \"field8146568203871\": \"0\",\n" +
                "    \"field4074568201624\": \"0\",\n" +
                "    \"field3116268195328\": \"0\",\n" +
                "    \"field6020768200089\": \"0\",\n" +
                "    \"field9412168198508\": \"0\",\n" +
                "    \"field7554868393615\": \"0\",\n" +
                "    \"field1063168382592\": \"0\",\n" +
                "    \"field2421868390183\": \"0\",\n" +
                "    \"field2330568391965\": \"0\",\n" +
                "    \"field9866268388652\": \"0\",\n" +
                "    \"field2722368386970\": \"0\",\n" +
                "    \"field6382368384992\": \"0\",\n" +
                "    \"field9226368379260\": \"0\",\n" +
                "    \"field7653168537232\": \"0\",\n" +
                "    \"field2065468543374\": \"0\",\n" +
                "    \"field4445968547255\": \"0\",\n" +
                "    \"field1570968545425\": \"0\",\n" +
                "    \"field3925768540926\": \"0\",\n" +
                "    \"field3035568638114\": \"0.0001\",\n" +
                "    \"field4964876200428\": \"0\",\n" +
                "    \"field6765076197431\": \"0\",\n" +
                "    \"field4517476198713\": \"0\",\n" +
                "    \"field2937476195617\": \"0\",\n" +
                "    \"field9388976191752\": \"0\",\n" +
                "    \"field5637876187458\": \"0\",\n" +
                "    \"field1559376190170\": \"0\",\n" +
                "    \"field5245676188721\": \"0\",\n" +
                "    \"field3956676186240\": \"0\",\n" +
                "    \"field3653776182010\": \"0\",\n" +
                "    \"field9837076185058\": \"0\",\n" +
                "    \"field8109976183641\": \"0\",\n" +
                "    \"field1035877076373\": \"0\",\n" +
                "    \"field7908277078038\": \"0\",\n" +
                "    \"field4191377074858\": \"0\",\n" +
                "    \"field7598577073043\": \"0\",\n" +
                "    \"field3885877062201\": \"0\",\n" +
                "    \"field9221077065648\": \"0\",\n" +
                "    \"field3074177210231\": \"1000\",\n" +
                "    \"field7549077208647\": \"50.1\",\n" +
                "    \"field5213777204503\": \"1\",\n" +
                "    \"field6747031756894\": \"统一社会信用代码\",\n" +
                "    \"field7785137366379\": \"萧山区档案局\",\n" +
                "    \"field4840031809132\": \"1601\",\n" +
                "    \"field8752031813294\": \"拱墅区006\",\n" +
                "    \"field1079331766366\": \"1601\"\n" +
                "  }";
        Map<String, Object> fieldData = JSON.parseObject(randomDataJson, Map.class);
        String json = "[{\"title\":\"基本情况表\",\"name\":\"TabsLayout\",\"icon\":\"sliders\",\"value\":[],\"valueType\":\"Array\",\"props\":{\"required\":false,\"enablePrint\":true,\"options\":[{\"name\":\"基本信息\",\"items\":[{\"title\":\"统一社会信用代码\",\"name\":\"TextInput\",\"icon\":\"edit\",\"value\":\"\",\"valueType\":\"String\",\"indicator\":\"\",\"props\":{\"required\":false,\"enablePrint\":true,\"name\":\"tyshxydm\"},\"id\":\"field6747031756894\"},{\"title\":\"单位名称\",\"name\":\"TextInput\",\"icon\":\"edit\",\"value\":\"\",\"valueType\":\"String\",\"indicator\":\"\",\"props\":{\"name\":\"dwmc\",\"required\":false,\"enablePrint\":true},\"id\":\"field7785137366379\"},{\"title\":\"单位类别代码\",\"name\":\"TextInput\",\"icon\":\"edit\",\"value\":\"\",\"valueType\":\"String\",\"indicator\":\"\",\"props\":{\"required\":false,\"enablePrint\":true},\"id\":\"field1079331766366\"},{\"title\":\"邮政编码\",\"name\":\"TextInput\",\"icon\":\"edit\",\"value\":\"\",\"valueType\":\"String\",\"indicator\":\"\",\"props\":{\"required\":false,\"enablePrint\":true,\"name\":\"yzbm\"},\"id\":\"field4840031809132\"},{\"title\":\"单位地址\",\"name\":\"TextInput\",\"icon\":\"edit\",\"value\":\"\",\"valueType\":\"String\",\"indicator\":\"\",\"props\":{\"required\":false,\"enablePrint\":true,\"name\":\"dwdz\"},\"id\":\"field8752031813294\"},{\"title\":\"电话\",\"name\":\"TextInput\",\"icon\":\"edit\",\"value\":\"\",\"valueType\":\"String\",\"indicator\":\"\",\"props\":{\"required\":false,\"enablePrint\":true},\"id\":\"field3104131870031\"},{\"title\":\"机构情况\",\"name\":\"SelectInput\",\"icon\":\"check-circle\",\"value\":\"\",\"valueType\":\"String\",\"indicator\":\"\",\"props\":{\"required\":false,\"enablePrint\":true,\"expanding\":false,\"options\":[\"选项1\",\"选项2\"]},\"id\":\"field3449031905754\"},{\"title\":\"年份\",\"name\":\"DateTime\",\"icon\":\"clock-circle\",\"value\":\"\",\"valueType\":\"Date\",\"indicator\":\"\",\"props\":{\"required\":false,\"enablePrint\":true,\"format\":\"YYYY\"},\"id\":\"field3091531931448\"}]},{\"name\":\"人员情况\",\"items\":[{\"title\":\"专职人员数\",\"name\":\"NumberInput\",\"icon\":\"form\",\"value\":\"\",\"valueType\":\"Number\",\"props\":{\"required\":false,\"enablePrint\":true},\"id\":\"field4382978544695\",\"indicator\":\"1837048222792040450\"},{\"title\":\"专职人员（女性）数\",\"name\":\"NumberInput\",\"icon\":\"form\",\"value\":\"\",\"valueType\":\"Number\",\"props\":{\"required\":false,\"enablePrint\":true},\"id\":\"field9075678543431\",\"indicator\":\"1837048352123404289\"},{\"title\":\"年龄（50岁以上）人数\",\"name\":\"NumberInput\",\"icon\":\"form\",\"value\":\"\",\"valueType\":\"Number\",\"props\":{\"required\":false,\"enablePrint\":true},\"id\":\"field3649978540632\",\"indicator\":\"1837048427016896514\"},{\"title\":\"年龄（35-49岁）人数\",\"name\":\"NumberInput\",\"icon\":\"form\",\"value\":\"\",\"valueType\":\"Number\",\"props\":{\"required\":false,\"enablePrint\":true},\"id\":\"field8213778542114\",\"indicator\":\"1837048471925309441\"},{\"title\":\"年龄（31岁以下）人数\",\"name\":\"NumberInput\",\"icon\":\"form\",\"value\":\"\",\"valueType\":\"Number\",\"props\":{\"required\":false,\"enablePrint\":true},\"id\":\"field5748878538036\",\"indicator\":\"1837048518503055362\"},{\"title\":\"专职人员博士研究生人数\",\"name\":\"NumberInput\",\"icon\":\"form\",\"value\":\"\",\"valueType\":\"Number\",\"props\":{\"required\":false,\"enablePrint\":true},\"id\":\"field5598478539417\",\"indicator\":\"1837052248518574082\"},{\"title\":\"专职人员硕士研究生人数\",\"name\":\"NumberInput\",\"icon\":\"form\",\"value\":\"\",\"valueType\":\"Number\",\"props\":{\"required\":false,\"enablePrint\":true},\"id\":\"field8017778536854\",\"indicator\":\"1837052287584321538\"},{\"title\":\"专职人员研究生班研究生人数\",\"name\":\"NumberInput\",\"icon\":\"form\",\"value\":\"\",\"valueType\":\"Number\",\"props\":{\"required\":false,\"enablePrint\":true},\"id\":\"field4944778535619\",\"indicator\":\"1837052331632902145\"},{\"title\":\"专职人员双学士人数\",\"name\":\"NumberInput\",\"icon\":\"form\",\"value\":\"\",\"valueType\":\"Number\",\"props\":{\"required\":false,\"enablePrint\":true},\"id\":\"field8789978530856\",\"indicator\":\"1837053494134587394\"},{\"title\":\"专职人员大学本科人数\",\"name\":\"NumberInput\",\"icon\":\"form\",\"value\":\"\",\"valueType\":\"Number\",\"props\":{\"required\":false,\"enablePrint\":true},\"id\":\"field8328778533454\",\"indicator\":\"1837053539948969986\"},{\"title\":\"专职人员大专人数\",\"name\":\"NumberInput\",\"icon\":\"form\",\"value\":\"\",\"valueType\":\"Number\",\"props\":{\"required\":false,\"enablePrint\":true},\"id\":\"field3161578532174\",\"indicator\":\"1837053586782568450\"},{\"title\":\"专职人员高中（含中专）及以下人数\",\"name\":\"NumberInput\",\"icon\":\"form\",\"value\":\"\",\"valueType\":\"Number\",\"props\":{\"required\":false,\"enablePrint\":true},\"id\":\"field4609978529441\",\"indicator\":\"1837054010172391425\"},{\"title\":\"专业文化程度博士研究生人数\",\"name\":\"NumberInput\",\"icon\":\"form\",\"value\":\"\",\"valueType\":\"Number\",\"props\":{\"required\":false,\"enablePrint\":true},\"id\":\"field5050278527526\",\"indicator\":\"1837054048462192642\"},{\"title\":\"专业文化程度硕士研究生人数\",\"name\":\"NumberInput\",\"icon\":\"form\",\"value\":\"\",\"valueType\":\"Number\",\"props\":{\"required\":false,\"enablePrint\":true},\"id\":\"field9284878709978\",\"indicator\":\"1837054102740680705\"},{\"title\":\"专业文化程度研究生班研究生人数\",\"name\":\"NumberInput\",\"icon\":\"form\",\"value\":\"\",\"valueType\":\"Number\",\"props\":{\"required\":false,\"enablePrint\":true},\"id\":\"field1274278711378\",\"indicator\":\"1837054143463178242\"},{\"title\":\"专业文化程度大学本科人数\",\"name\":\"NumberInput\",\"icon\":\"form\",\"value\":\"\",\"valueType\":\"Number\",\"props\":{\"required\":false,\"enablePrint\":true},\"id\":\"field4375578701885\",\"indicator\":\"1837054992058957826\"},{\"title\":\"专业文化程度大专人数\",\"name\":\"NumberInput\",\"icon\":\"form\",\"value\":\"\",\"valueType\":\"Number\",\"props\":{\"required\":false,\"enablePrint\":true},\"id\":\"field9521978707515\",\"indicator\":\"1837055045305647105\"},{\"title\":\"专业文化程度中专人数\",\"name\":\"NumberInput\",\"icon\":\"form\",\"value\":\"\",\"valueType\":\"Number\",\"props\":{\"required\":false,\"enablePrint\":true},\"id\":\"field8436078706113\",\"indicator\":\"1837055100259418113\"},{\"title\":\"档案干部专业技术研究馆员数\",\"name\":\"NumberInput\",\"icon\":\"form\",\"value\":\"\",\"valueType\":\"Number\",\"props\":{\"required\":false,\"enablePrint\":true},\"id\":\"field2135478704798\",\"indicator\":\"1837055168861454337\"},{\"title\":\"档案干部专业技术副研究馆员数\",\"name\":\"NumberInput\",\"icon\":\"form\",\"value\":\"\",\"valueType\":\"Number\",\"props\":{\"required\":false,\"enablePrint\":true},\"id\":\"field7489178703383\",\"indicator\":\"1837055216722657281\"},{\"title\":\"档案干部专业技术馆员数\",\"name\":\"NumberInput\",\"icon\":\"form\",\"value\":\"\",\"valueType\":\"Number\",\"props\":{\"required\":false,\"enablePrint\":true},\"id\":\"field4159078699535\",\"indicator\":\"1837055261232611329\"},{\"title\":\"档案干部专业技术助理馆员数\",\"name\":\"NumberInput\",\"icon\":\"form\",\"value\":\"\",\"valueType\":\"Number\",\"props\":{\"required\":false,\"enablePrint\":true},\"id\":\"field8680378807013\",\"indicator\":\"1837055306396876801\"},{\"title\":\"档案干部专业技术管理员数\",\"name\":\"NumberInput\",\"icon\":\"form\",\"value\":\"\",\"valueType\":\"Number\",\"props\":{\"required\":false,\"enablePrint\":true},\"id\":\"field9501878804399\",\"indicator\":\"1837055354618789889\"},{\"title\":\"兼职人员\",\"name\":\"NumberInput\",\"icon\":\"form\",\"value\":\"\",\"valueType\":\"Number\",\"props\":{\"required\":false,\"enablePrint\":true},\"id\":\"field4386165146790\",\"indicator\":\"1837055395551002626\"},{\"title\":\"本年接受档案业务在职培训教育期数\",\"name\":\"NumberInput\",\"icon\":\"form\",\"value\":\"\",\"valueType\":\"Number\",\"props\":{\"required\":false,\"enablePrint\":true},\"id\":\"field1516565153234\",\"indicator\":\"1837055435690491905\"},{\"title\":\"本年接受档案业务在职培训教育期数\",\"name\":\"NumberInput\",\"icon\":\"form\",\"value\":\"\",\"valueType\":\"Number\",\"props\":{\"required\":false,\"enablePrint\":true},\"id\":\"field9955765150037\",\"indicator\":\"1837055435690491905\"}]},{\"name\":\"室存情况\",\"items\":[{\"title\":\"纸质档案总计卷数（卷）\",\"name\":\"NumberInput\",\"icon\":\"form\",\"value\":\"\",\"valueType\":\"Number\",\"props\":{\"required\":false,\"enablePrint\":true},\"id\":\"field3825966228931\",\"indicator\":\"1837481250941059073\"},{\"title\":\"纸质档案总计件数（件）\",\"name\":\"NumberInput\",\"icon\":\"form\",\"value\":\"\",\"valueType\":\"Number\",\"props\":{\"required\":false,\"enablePrint\":true},\"id\":\"field5814466232194\",\"indicator\":\"1837481585667489793\"},{\"title\":\"总排架长度米数（米）（手填）\",\"name\":\"NumberInput\",\"icon\":\"form\",\"value\":\"\",\"valueType\":\"Number\",\"props\":{\"required\":false,\"enablePrint\":true},\"id\":\"field2755666230562\"},{\"title\":\"电子档案总计容量（GB)\",\"name\":\"NumberInput\",\"icon\":\"form\",\"value\":\"\",\"valueType\":\"Number\",\"props\":{\"required\":false,\"enablePrint\":true},\"id\":\"field6367566472166\",\"indicator\":\"1837481654735093761\"},{\"title\":\"电子档案文书类总计件数\",\"name\":\"NumberInput\",\"icon\":\"form\",\"value\":\"\",\"valueType\":\"Number\",\"props\":{\"required\":false,\"enablePrint\":true},\"id\":\"field7864166469753\",\"indicator\":\"1837481697634435074\"},{\"title\":\"电子档案文书类总计容量(GB)\",\"name\":\"NumberInput\",\"icon\":\"form\",\"value\":\"\",\"valueType\":\"Number\",\"props\":{\"required\":false,\"enablePrint\":true},\"id\":\"field5978666474298\",\"indicator\":\"1837481823362891777\"},{\"title\":\"电子档案数码照片总计张数\",\"name\":\"NumberInput\",\"icon\":\"form\",\"value\":\"\",\"valueType\":\"Number\",\"props\":{\"required\":false,\"enablePrint\":true},\"id\":\"field1517766266250\",\"indicator\":\"1837497800104361985\"},{\"title\":\"电子档案数码照片总计容量（GB）\",\"name\":\"NumberInput\",\"icon\":\"form\",\"value\":\"\",\"valueType\":\"Number\",\"props\":{\"required\":false,\"enablePrint\":true},\"id\":\"field7381467020828\",\"indicator\":\"1837497800104361985\"},{\"title\":\"电子档案数字录音、数字录像总计小时数\",\"name\":\"NumberInput\",\"icon\":\"form\",\"value\":\"\",\"valueType\":\"Number\",\"props\":{\"required\":false,\"enablePrint\":true},\"id\":\"field9691378000498\",\"indicator\":\"1837497921328136194\"},{\"title\":\"电子档案数字录音、数字录像总计容量（GB)\",\"name\":\"NumberInput\",\"icon\":\"form\",\"value\":\"\",\"valueType\":\"Number\",\"props\":{\"required\":false,\"enablePrint\":true},\"id\":\"field3508178002613\",\"indicator\":\"1837497960641347586\"},{\"title\":\"其他载体档案照片档案总计张数 \",\"name\":\"NumberInput\",\"icon\":\"form\",\"value\":\"\",\"valueType\":\"Number\",\"props\":{\"required\":false,\"enablePrint\":true},\"id\":\"field2313777993834\",\"indicator\":\"1837498011690221569\"},{\"title\":\"其他载体录音磁带、录像磁带、影片档案总计盘（件）数\",\"name\":\"NumberInput\",\"icon\":\"form\",\"value\":\"\",\"valueType\":\"Number\",\"props\":{\"required\":false,\"enablePrint\":true},\"id\":\"field9150167148706\",\"indicator\":\"1837498051871653890\"},{\"title\":\"实物档案总计件数\",\"name\":\"NumberInput\",\"icon\":\"form\",\"value\":\"\",\"valueType\":\"Number\",\"props\":{\"required\":false,\"enablePrint\":true},\"id\":\"field8396367249508\",\"indicator\":\"1837498101003730945\"},{\"title\":\"纸质档案其中室存永久卷数\",\"name\":\"NumberInput\",\"icon\":\"form\",\"value\":\"\",\"valueType\":\"Number\",\"props\":{\"required\":false,\"enablePrint\":true},\"id\":\"field8250767626829\",\"indicator\":\"1837498151125663746\"},{\"title\":\"纸质档案其中室存永久件数\",\"name\":\"NumberInput\",\"icon\":\"form\",\"value\":\"\",\"valueType\":\"Number\",\"props\":{\"required\":false,\"enablePrint\":true},\"id\":\"field3067567647912\",\"indicator\":\"1837498194075336705\"},{\"title\":\"电子档案其中室存永久容量（GB)\",\"name\":\"NumberInput\",\"icon\":\"form\",\"value\":\"\",\"valueType\":\"Number\",\"props\":{\"required\":false,\"enablePrint\":true},\"id\":\"field6912467656557\",\"indicator\":\"1837498239327682562\"},{\"title\":\"电子档案文书类其中室存永久件数\",\"name\":\"NumberInput\",\"icon\":\"form\",\"value\":\"\",\"valueType\":\"Number\",\"props\":{\"required\":false,\"enablePrint\":true},\"id\":\"field2535667657822\",\"indicator\":\"1837498286589100033\"},{\"title\":\"电子档案文书类其中室存永久容量(GB)\",\"name\":\"NumberInput\",\"icon\":\"form\",\"value\":\"\",\"valueType\":\"Number\",\"props\":{\"required\":false,\"enablePrint\":true},\"id\":\"field4244267652742\",\"indicator\":\"1837498331052916738\"},{\"title\":\"电子档案数码照片其中室存永久张数\",\"name\":\"NumberInput\",\"icon\":\"form\",\"value\":\"\",\"valueType\":\"Number\",\"props\":{\"required\":false,\"enablePrint\":true},\"id\":\"field2807467655355\",\"indicator\":\"1837498404281270273\"},{\"title\":\"电子档案数码照片其中室存永久容量（GB）\",\"name\":\"NumberInput\",\"icon\":\"form\",\"value\":\"\",\"valueType\":\"Number\",\"props\":{\"required\":false,\"enablePrint\":true},\"id\":\"field4874467654107\",\"indicator\":\"1837498445922320386\"},{\"title\":\"电子档案数字录音、数字录像其中室存永久小时数\",\"name\":\"NumberInput\",\"icon\":\"form\",\"value\":\"\",\"valueType\":\"Number\",\"props\":{\"required\":false,\"enablePrint\":true},\"id\":\"field5189267651512\",\"indicator\":\"1837498538704519170\"},{\"title\":\"电子档案数字录音、数字录像其中室存永久容量（GB)\",\"name\":\"NumberInput\",\"icon\":\"form\",\"value\":\"\",\"valueType\":\"Number\",\"props\":{\"required\":false,\"enablePrint\":true},\"id\":\"field4670467646084\",\"indicator\":\"1837499031174529026\"},{\"title\":\"其他载体档案照片档案其中室存永久张数 \",\"name\":\"NumberInput\",\"icon\":\"form\",\"value\":\"\",\"valueType\":\"Number\",\"props\":{\"required\":false,\"enablePrint\":true},\"id\":\"field1984467649328\",\"indicator\":\"1837499087617277953\"},{\"title\":\"其他载体档案录音磁带、录像磁带、影片档案其中室存永久盘（件）数\",\"name\":\"NumberInput\",\"icon\":\"form\",\"value\":\"\",\"valueType\":\"Number\",\"props\":{\"required\":false,\"enablePrint\":true},\"id\":\"field6459467629228\",\"indicator\":\"1837499135998574593\"},{\"title\":\"实物档案其中室存永久件数\",\"name\":\"NumberInput\",\"icon\":\"form\",\"value\":\"\",\"valueType\":\"Number\",\"props\":{\"required\":false,\"enablePrint\":true},\"id\":\"field6823368207734\",\"indicator\":\"1837499184790913025\"},{\"title\":\"纸质档案其中室存30年（长期）卷数\",\"name\":\"NumberInput\",\"icon\":\"form\",\"value\":\"\",\"valueType\":\"Number\",\"props\":{\"required\":false,\"enablePrint\":true},\"id\":\"field4052068214445\",\"indicator\":\"1837499263312478209\"},{\"title\":\"纸质档案其中室存30年（长期）件数\",\"name\":\"NumberInput\",\"icon\":\"form\",\"value\":\"\",\"valueType\":\"Number\",\"props\":{\"required\":false,\"enablePrint\":true},\"id\":\"field3037968212946\",\"indicator\":\"1837499313543462914\"},{\"title\":\"电子档案其中室存30年（长期）容量（GB)\",\"name\":\"NumberInput\",\"icon\":\"form\",\"value\":\"\",\"valueType\":\"Number\",\"props\":{\"required\":false,\"enablePrint\":true},\"id\":\"field5166168205952\",\"indicator\":\"1837499396456464385\"},{\"title\":\"电子档案文书类其中室存30年（长期）件数\",\"name\":\"NumberInput\",\"icon\":\"form\",\"value\":\"\",\"valueType\":\"Number\",\"props\":{\"required\":false,\"enablePrint\":true},\"id\":\"field5157468211597\",\"indicator\":\"1837499451724808194\"},{\"title\":\"电子档案文书类其中室存30年（长期）容量(GB)\",\"name\":\"NumberInput\",\"icon\":\"form\",\"value\":\"\",\"valueType\":\"Number\",\"props\":{\"required\":false,\"enablePrint\":true},\"id\":\"field8146568203871\",\"indicator\":\"1837499493919506433\"},{\"title\":\"电子档案数码照片其中室存30年（长期）张数\",\"name\":\"NumberInput\",\"icon\":\"form\",\"value\":\"\",\"valueType\":\"Number\",\"props\":{\"required\":false,\"enablePrint\":true},\"id\":\"field4074568201624\",\"indicator\":\"1837499540954431490\"},{\"title\":\"电子档案数码照片其中室存30年（长期）永久容量（GB）\",\"name\":\"NumberInput\",\"icon\":\"form\",\"value\":\"\",\"valueType\":\"Number\",\"props\":{\"required\":false,\"enablePrint\":true},\"id\":\"field3116268195328\",\"indicator\":\"1837499591843921921\"},{\"title\":\"电子档案数字录音、数字录像其中室存30年（长期）永久小时数\",\"name\":\"NumberInput\",\"icon\":\"form\",\"value\":\"\",\"valueType\":\"Number\",\"props\":{\"required\":false,\"enablePrint\":true},\"id\":\"field6020768200089\",\"indicator\":\"1837499662505361410\"},{\"title\":\"电子档案数字录音、数字录像其中室存30年（长期）容量（GB)\",\"name\":\"NumberInput\",\"icon\":\"form\",\"value\":\"\",\"valueType\":\"Number\",\"props\":{\"required\":false,\"enablePrint\":true},\"id\":\"field9412168198508\",\"indicator\":\"1837499723326963713\"},{\"title\":\"其他载体档案照片档案其中室存30年（长期）张数 \",\"name\":\"NumberInput\",\"icon\":\"form\",\"value\":\"\",\"valueType\":\"Number\",\"props\":{\"required\":false,\"enablePrint\":true},\"id\":\"field7554868393615\",\"indicator\":\"1837499892533575681\"},{\"title\":\"其他载体档案录音磁带、录像磁带、影片档案其中室存30年（长期）盘（件）数\",\"name\":\"NumberInput\",\"icon\":\"form\",\"value\":\"\",\"valueType\":\"Number\",\"props\":{\"required\":false,\"enablePrint\":true},\"id\":\"field1063168382592\",\"indicator\":\"1837499938477981697\"},{\"title\":\"实物档案其中室存30年（长期）件数\",\"name\":\"NumberInput\",\"icon\":\"form\",\"value\":\"\",\"valueType\":\"Number\",\"props\":{\"required\":false,\"enablePrint\":true},\"id\":\"field2421868390183\",\"indicator\":\"1837499985663901697\"},{\"title\":\"档案数字化成果纸质档案卷数\",\"name\":\"NumberInput\",\"icon\":\"form\",\"value\":\"\",\"valueType\":\"Number\",\"props\":{\"required\":false,\"enablePrint\":true},\"id\":\"field2330568391965\",\"indicator\":\"1837500026025689090\"},{\"title\":\"档案数字化成果纸质档案卷附件容量\",\"name\":\"NumberInput\",\"icon\":\"form\",\"value\":\"\",\"valueType\":\"Number\",\"props\":{\"required\":false,\"enablePrint\":true},\"id\":\"field9866268388652\",\"indicator\":\"1837500076604801026\"},{\"title\":\"档案数字化成果纸质档案卷附件万幅数\",\"name\":\"NumberInput\",\"icon\":\"form\",\"value\":\"\",\"valueType\":\"Number\",\"props\":{\"required\":false,\"enablePrint\":true},\"id\":\"field2722368386970\",\"indicator\":\"1837500133819301889\"},{\"title\":\"档案数字化成果纸质档案件数\",\"name\":\"NumberInput\",\"icon\":\"form\",\"value\":\"\",\"valueType\":\"Number\",\"props\":{\"required\":false,\"enablePrint\":true},\"id\":\"field6382368384992\",\"indicator\":\"1837500192204013569\"},{\"title\":\"档案数字化成果纸质档案件附件容量\",\"name\":\"NumberInput\",\"icon\":\"form\",\"value\":\"\",\"valueType\":\"Number\",\"props\":{\"required\":false,\"enablePrint\":true},\"id\":\"field9226368379260\",\"indicator\":\"1837500295195148290\"},{\"title\":\"档案数字化成果纸质档案件附件万幅数\",\"name\":\"NumberInput\",\"icon\":\"form\",\"value\":\"\",\"valueType\":\"Number\",\"props\":{\"required\":false,\"enablePrint\":true},\"id\":\"field7653168537232\",\"indicator\":\"1837500348408283137\"},{\"title\":\"档案数字化成果照片档案附件容量\",\"name\":\"NumberInput\",\"icon\":\"form\",\"value\":\"\",\"valueType\":\"Number\",\"props\":{\"required\":false,\"enablePrint\":true},\"id\":\"field2065468543374\",\"indicator\":\"1837500397615857666\"},{\"title\":\"档案数字化成果录音磁带、录像磁带、影片档案档案附件容量\",\"name\":\"NumberInput\",\"icon\":\"form\",\"value\":\"\",\"valueType\":\"Number\",\"props\":{\"required\":false,\"enablePrint\":true},\"id\":\"field4445968547255\",\"indicator\":\"1837500450040463361\"},{\"title\":\"档案数字化成果其他档案附件容量\",\"name\":\"NumberInput\",\"icon\":\"form\",\"value\":\"\",\"valueType\":\"Number\",\"props\":{\"required\":false,\"enablePrint\":true},\"id\":\"field1570968545425\",\"indicator\":\"1837500502641229825\"},{\"title\":\"档案编目情况机读目录案卷级万条数\",\"name\":\"NumberInput\",\"icon\":\"form\",\"value\":\"\",\"valueType\":\"Number\",\"props\":{\"required\":false,\"enablePrint\":true},\"id\":\"field3925768540926\",\"indicator\":\"1837500545918058498\"},{\"title\":\"档案编目情况机读目录文件级万条数\",\"name\":\"NumberInput\",\"icon\":\"form\",\"value\":\"\",\"valueType\":\"Number\",\"props\":{\"required\":false,\"enablePrint\":true},\"id\":\"field3035568638114\",\"indicator\":\"1837500589366853633\"},{\"title\":\"检索工具总计数（手填）\",\"name\":\"NumberInput\",\"icon\":\"form\",\"value\":\"\",\"valueType\":\"Number\",\"props\":{\"required\":false,\"enablePrint\":true},\"id\":\"field2176668635432\"}]},{\"name\":\"移交接收\",\"items\":[{\"title\":\"本年度接收纸质档案总计卷数\",\"name\":\"NumberInput\",\"icon\":\"form\",\"value\":\"\",\"valueType\":\"Number\",\"props\":{\"required\":false,\"enablePrint\":true},\"id\":\"field4964876200428\",\"indicator\":\"1837860062983897090\"},{\"title\":\"本年度接收纸质档案总计件数\",\"name\":\"NumberInput\",\"icon\":\"form\",\"value\":\"\",\"valueType\":\"Number\",\"props\":{\"required\":false,\"enablePrint\":true},\"id\":\"field6765076197431\",\"indicator\":\"1837860117002338305\"},{\"title\":\"本年度接收电子档案总计容量（GB)\",\"name\":\"NumberInput\",\"icon\":\"form\",\"value\":\"\",\"valueType\":\"Number\",\"props\":{\"required\":false,\"enablePrint\":true},\"id\":\"field4517476198713\",\"indicator\":\"1837860162825109505\"},{\"title\":\"本年度接收电子档案文书类总计件数\",\"name\":\"NumberInput\",\"icon\":\"form\",\"value\":\"\",\"valueType\":\"Number\",\"props\":{\"required\":false,\"enablePrint\":true},\"id\":\"field2937476195617\",\"indicator\":\"1837860220446457858\"},{\"title\":\"本年度接收电子档案文书类总计容量(GB)\",\"name\":\"NumberInput\",\"icon\":\"form\",\"value\":\"\",\"valueType\":\"Number\",\"props\":{\"required\":false,\"enablePrint\":true},\"id\":\"field9388976191752\",\"indicator\":\"1837860268374769665\"},{\"title\":\"本年度接收电子档案数码照片总计张数\",\"name\":\"NumberInput\",\"icon\":\"form\",\"value\":\"\",\"valueType\":\"Number\",\"props\":{\"required\":false,\"enablePrint\":true},\"id\":\"field5637876187458\",\"indicator\":\"1837860311597072386\"},{\"title\":\"本年度接收电子档案数码照片总计容量（GB）\",\"name\":\"NumberInput\",\"icon\":\"form\",\"value\":\"\",\"valueType\":\"Number\",\"props\":{\"required\":false,\"enablePrint\":true},\"id\":\"field1559376190170\",\"indicator\":\"1837860354865512449\"},{\"title\":\"本年度接收电子档案数字录音、数字录像总计小时数\",\"name\":\"NumberInput\",\"icon\":\"form\",\"value\":\"\",\"valueType\":\"Number\",\"props\":{\"required\":false,\"enablePrint\":true},\"id\":\"field5245676188721\",\"indicator\":\"1837860394250027009\"},{\"title\":\"本年度接收电子档案数字录音、数字录像总计容量（GB)\",\"name\":\"NumberInput\",\"icon\":\"form\",\"value\":\"\",\"valueType\":\"Number\",\"props\":{\"required\":false,\"enablePrint\":true},\"id\":\"field3956676186240\",\"indicator\":\"1837860438952919042\"},{\"title\":\"本年度接收其他载体档案照片档案总计张数 \",\"name\":\"NumberInput\",\"icon\":\"form\",\"value\":\"\",\"valueType\":\"Number\",\"props\":{\"required\":false,\"enablePrint\":true},\"id\":\"field3653776182010\",\"indicator\":\"1837860479386009602\"},{\"title\":\"本年度接收其他载体档案录音磁带、录像磁带、影片档案总计盘（件）数\",\"name\":\"NumberInput\",\"icon\":\"form\",\"value\":\"\",\"valueType\":\"Number\",\"props\":{\"required\":false,\"enablePrint\":true},\"id\":\"field9837076185058\",\"indicator\":\"1837860525665959937\"},{\"title\":\"本年度接收实物档案总计件数\",\"name\":\"NumberInput\",\"icon\":\"form\",\"value\":\"\",\"valueType\":\"Number\",\"props\":{\"required\":false,\"enablePrint\":true},\"id\":\"field8109976183641\",\"indicator\":\"1837860562760384514\"},{\"title\":\"本年是否向档案馆移交档案（手填）\",\"name\":\"SelectInput\",\"icon\":\"check-circle\",\"value\":\"\",\"valueType\":\"String\",\"props\":{\"required\":false,\"enablePrint\":true,\"expanding\":false,\"options\":[\"是\",\"否\"]},\"id\":\"field2410176516087\"},{\"title\":\"本年是否有移出档案（手填）\",\"name\":\"SelectInput\",\"icon\":\"check-circle\",\"value\":\"\",\"valueType\":\"String\",\"props\":{\"required\":false,\"enablePrint\":true,\"expanding\":false,\"options\":[\"是\",\"否\"]},\"id\":\"field2458076518918\"},{\"title\":\"本年是否有销毁档案（手填）\",\"name\":\"SelectInput\",\"icon\":\"check-circle\",\"value\":\"\",\"valueType\":\"String\",\"props\":{\"required\":false,\"enablePrint\":true,\"expanding\":false,\"options\":[\"是\",\"否\"]},\"id\":\"field7862676521997\"}]},{\"name\":\"利用情况\",\"items\":[{\"title\":\"本年利用档案人次\",\"name\":\"NumberInput\",\"icon\":\"form\",\"value\":\"\",\"valueType\":\"Number\",\"props\":{\"required\":false,\"enablePrint\":true,\"placeholder\":\"\"},\"id\":\"field1035877076373\",\"indicator\":\"1838056435851214849\"},{\"title\":\"本年利用档案卷（件）次\",\"name\":\"NumberInput\",\"icon\":\"form\",\"value\":\"\",\"valueType\":\"Number\",\"props\":{\"required\":false,\"enablePrint\":true},\"id\":\"field7908277078038\",\"indicator\":\"1838056529841373185\"},{\"title\":\"本年编研成果公开出版数\",\"name\":\"NumberInput\",\"icon\":\"form\",\"value\":\"\",\"valueType\":\"Number\",\"props\":{\"required\":false,\"enablePrint\":true},\"id\":\"field4191377074858\",\"indicator\":\"1838056729548963842\"},{\"title\":\"本年编研成果公开出版字数（万字）\",\"name\":\"NumberInput\",\"icon\":\"form\",\"value\":\"\",\"valueType\":\"Number\",\"props\":{\"required\":false,\"enablePrint\":true},\"id\":\"field7598577073043\",\"indicator\":\"1838056729548963842\"},{\"title\":\"本年编研内部参考出版数\",\"name\":\"NumberInput\",\"icon\":\"form\",\"value\":\"\",\"valueType\":\"Number\",\"props\":{\"required\":false,\"enablePrint\":true},\"id\":\"field3885877062201\",\"indicator\":\"1838057280533708801\"},{\"title\":\"本年编研内部参考出版字数（万字）\",\"name\":\"NumberInput\",\"icon\":\"form\",\"value\":\"\",\"valueType\":\"Number\",\"props\":{\"required\":false,\"enablePrint\":true},\"id\":\"field9221077065648\",\"indicator\":\"1838057280533708801\"}]},{\"name\":\"设施设备\",\"items\":[{\"title\":\"档案室建筑面积（平方米）\",\"name\":\"NumberInput\",\"icon\":\"form\",\"value\":\"\",\"valueType\":\"Number\",\"props\":{\"required\":false,\"enablePrint\":true},\"id\":\"field3074177210231\",\"indicator\":\"1838058593761902593\"},{\"title\":\"档案库房建筑面积（平方米）\",\"name\":\"NumberInput\",\"icon\":\"form\",\"value\":\"\",\"valueType\":\"Number\",\"props\":{\"required\":false,\"enablePrint\":true},\"id\":\"field7549077208647\",\"indicator\":\"1838058646916317186\"},{\"title\":\"档案室设备服务器台数（手填）\",\"name\":\"NumberInput\",\"icon\":\"form\",\"value\":\"\",\"valueType\":\"Number\",\"props\":{\"required\":false,\"enablePrint\":true},\"id\":\"field1367777202821\"},{\"title\":\"档案室设备火灾自动报警系统套数（手填）\",\"name\":\"NumberInput\",\"icon\":\"form\",\"value\":\"\",\"valueType\":\"Number\",\"props\":{\"required\":false,\"enablePrint\":true},\"id\":\"field5023177207101\"},{\"title\":\"档案室设备温湿度控制系统套数（手填）\",\"name\":\"NumberInput\",\"icon\":\"form\",\"value\":\"\",\"valueType\":\"Number\",\"props\":{\"required\":false,\"enablePrint\":true},\"id\":\"field5206477205884\"},{\"title\":\"数字档案室（个）\",\"name\":\"NumberInput\",\"icon\":\"form\",\"value\":\"\",\"valueType\":\"Number\",\"props\":{\"required\":false,\"enablePrint\":true},\"id\":\"field5213777204503\",\"indicator\":\"1838058694957875202\"}]}],\"type\":\"button\",\"name\":\"单位名称\"},\"id\":\"field5630662546516\"}]";
        DocumentContext documentContext = JsonPath.parse(json);
        JSONArray items = documentContext.read("$..items[*]");

        Map<String, Object> resultData = new LinkedHashMap<>();
        for (int i = 0; i < items.size(); i++) {
            Map<String, Object> itemData = (Map<String, Object>) items.get(i);
            if(Objects.isNull(itemData)){
                continue;
            }
            String fieldNameCN = (String) itemData.get("title");
            String randomFieldName = (String) itemData.get("id");
            // System.out.printf("{%s}", fieldNameCN);
            Object value = fieldData.get(randomFieldName);
            if(Objects.nonNull(value)){
                resultData.put(fieldNameCN, value);
            }

            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            // System.out.print("\r");
            System.out.print(String.format("进度：%d/%d", i, items.size()));
        }


        InputStream templateStream = InputStreamUtils.getByteArrayInputStreamFromFile(new File(TEMPLATE_LOCAL_PATH));
        File excelFile = createExcelFileFromTempPath();

        try {
            ExcelWriter excelWriter = EasyExcel.write(excelFile).withTemplate(templateStream).build();

            WriteSheet writeSheet = EasyExcel.writerSheet(0).build();
            excelWriter.fill(resultData, writeSheet);

            excelWriter.finish();
            LogUtil.info("年报文件已生成");
        } catch (Exception e) {
            LogUtil.error("生成年报文件未知异常", e);
        }
        LogUtil.info("");
    }


    private static File createExcelFileFromTempPath() {
        File excelFile = null;
        String excelFileName = "档案室年报统计" + System.currentTimeMillis() + ".xls";
        try {
            File tempFolder = Files.createTempDirectory("year_report_").toFile();
            String failedFilePath = tempFolder.getPath() + File.separator + excelFileName;
            excelFile = new File(failedFilePath);
            excelFile.createNewFile();
        } catch (IOException e) {
            LogUtil.error("生成年报导出临时文件失败", e);
        }
        return excelFile;
    }

}
