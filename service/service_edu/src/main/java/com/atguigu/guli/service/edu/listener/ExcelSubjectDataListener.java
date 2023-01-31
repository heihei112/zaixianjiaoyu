package com.atguigu.guli.service.edu.listener;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.atguigu.guli.service.edu.entity.Subject;
import com.atguigu.guli.service.edu.entity.excel.ExcelSubjectData;
import com.atguigu.guli.service.edu.mapper.SubjectMapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.objenesis.instantiator.android.AndroidSerializationInstantiator;

import java.util.HashSet;

@Slf4j
@AllArgsConstructor
@NoArgsConstructor
public class ExcelSubjectDataListener extends AnalysisEventListener<ExcelSubjectData> {

    private SubjectMapper subjectMapper;

    @Override

    public void invoke(ExcelSubjectData data, AnalysisContext analysisContext) {
        log.info("解析到一条数据:{}",data);
        String levelOneTitle = data.getLevelOneTitle();
        String levelTwoTitle = data.getLevelTwoTitle();
        log.info("levelOneTitle:{}",levelOneTitle);
        log.info("levelTwoTitle:{}",levelTwoTitle);

        String parentId = null;
        Subject byTitle = getSubByTitle(levelOneTitle,"0");
        if (byTitle == null){
            Subject subject = new Subject();
            subject.setParentId("0");

            subject.setTitle(levelOneTitle);
            subjectMapper.insert(subject);
            parentId = subject.getId();
        } else {
            parentId =  byTitle.getId();
        }
        Subject subByTitle = getSubByTitle(levelOneTitle, parentId);
        if (subByTitle==null){
            Subject subject = new Subject();
            subject.setTitle(levelTwoTitle);
            subject.setParentId(parentId);
            subjectMapper.insert(subject);
        }

    }

    @Override
    public void doAfterAllAnalysed(AnalysisContext analysisContext) {
        log.info("上传完毕");
    }


    public Subject getSubByTitle(String title , String parentId) {
        QueryWrapper<Subject> sub = new QueryWrapper<Subject>()
                .eq("title",title)
                .eq("parent_id",parentId);
        return subjectMapper.selectOne(sub);
    }
}
