package com.owl.web.dao.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.owl.web.dao.entity.TbHistory;
import com.owl.web.dao.mapper.TbHistoryMapper;
import com.owl.web.dao.service.TbHistoryService;
import org.springframework.stereotype.Service;

@Service
public class TbHistoryServiceImpl extends ServiceImpl<TbHistoryMapper, TbHistory> implements TbHistoryService {

}
