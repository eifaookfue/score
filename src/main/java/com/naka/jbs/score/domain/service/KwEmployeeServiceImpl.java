package com.naka.jbs.score.domain.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.naka.jbs.score.domain.model.entity.score.KwEmployee;
import com.naka.jbs.score.domain.repository.score.KwEmployeeRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class KwEmployeeServiceImpl implements KwEmployeeService {

    private final KwEmployeeRepository kwEmployeeRepository;

    @Override
    public List<KwEmployee> getAll() {
        return kwEmployeeRepository.findAll();
    }

}
