package com.example.codestates.member.service;

import com.example.codestates.bgcolor.entity.BgColor;
import com.example.codestates.bgcolor.service.BgColorService;
import com.example.codestates.exception.BusinessLogicException;
import com.example.codestates.exception.ExceptionCode;
import com.example.codestates.member.repository.MemberRepository;
import com.example.codestates.member.entity.Member;
import com.example.codestates.auth.utils.CustomAuthorityUtils;
import com.example.codestates.utils.CustomBeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.constraints.Positive;
import java.util.Optional;

@Service
@Transactional
public class MemberService {
    private final MemberRepository memberRepository;
    private final BgColorService bgColorService; // BgColorService를 주입
    private final CustomBeanUtils<Member> beanUtils;
    private final PasswordEncoder passwordEncoder;
    private final CustomAuthorityUtils authorityUtils;

    //생성자 DI용 파리미터 추가
    public MemberService(MemberRepository memberRepository, BgColorService bgColorService, BgColorService bgColorService1, CustomBeanUtils<Member> beanUtils, PasswordEncoder passwordEncoder, CustomAuthorityUtils authorityUtils) {
        this.memberRepository = memberRepository;
        this.bgColorService = bgColorService1;
        this.beanUtils = beanUtils;
        this.passwordEncoder = passwordEncoder;
        this.authorityUtils = authorityUtils;
    }

    // 회원 생성
    public Member createMember(Member member) {
        verifyExistEmail(member.getEmail());
        verifyExistNickName(member.getNickname());
        //패스워드 암호화
        String encryptedPassword = passwordEncoder.encode(member.getPassword());
        member.setPassword(encryptedPassword);

        // 추가: DB에 User Role 저장
        String role = authorityUtils.createRoles(member.getEmail());
        member.setRole(role);

        // 추가: 기본 배경색 설정
        BgColor defaultColor = bgColorService.getDefaultBgColor();
        member.setBgColor(defaultColor);

        return memberRepository.save(member);
    }

    //회원 정보 수정
    public Member updateMember(Member member, @Positive long memberId) {
        // 회원 정보 찾기
        Member foundMember = memberRepository.findById(memberId)
                .orElseThrow(() -> new BusinessLogicException(ExceptionCode.MEMBER_NOT_FOUND));

        // 정보 업데이트
        Member updatedMember = beanUtils.copyNonNullProperties(member, foundMember);
        return memberRepository.save(updatedMember);
    }


    //회원 삭제
    public void deleteMember(@Positive long memberId) {
        Member foundMember = memberRepository.findById(memberId)
                .orElseThrow(() -> new BusinessLogicException(ExceptionCode.MEMBER_NOT_FOUND));
        memberRepository.delete(foundMember);
    }

    public Page<Member> findMembers(String type, int page, int size) {
        PageRequest pageRequest = PageRequest.of(page, size);
        // 회원 목록 조회

        return memberRepository.findAll(pageRequest);
    }

    // 유효성 검사
    private void verifyExistEmail(String email) {
        Optional<Member> member = memberRepository.findByEmail(email);
        if (member.isPresent()) {
            throw new IllegalArgumentException("Email already exists");
        }
    }

    private void verifyExistNickName(String nickname) {
        Optional<Member> member = memberRepository.findByNickname(nickname);
        if (member.isPresent()) {
            throw new IllegalArgumentException("Nickname already exists");
        }
    }
}