package com.kh.app.web;

import com.kh.app.domain.entity.Member;
import com.kh.app.domain.member.svc.MemberSVC;
import com.kh.app.web.common.CodeDecode;
import com.kh.app.web.form.member.JoinForm;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.apache.bcel.classfile.Code;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.thymeleaf.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("/members")
public class MemberController {

  private final MemberSVC memberSVC;


  @ModelAttribute("hobbies")
  public List<CodeDecode> hobbies() {
    List<CodeDecode> codes = new ArrayList<>();
    codes.add(new CodeDecode("축구","축구"));
    codes.add(new CodeDecode("낚시","낚시"));
    codes.add(new CodeDecode("드라이브","드라이브"));
    codes.add(new CodeDecode("음악감상","음악감상"));
    return codes;
  }

  @ModelAttribute("regions")
  public List<CodeDecode> regions() {
    List<CodeDecode> codes = new ArrayList<>();
    codes.add(new CodeDecode("서울","서울"));
    codes.add(new CodeDecode("부산","부산"));
    codes.add(new CodeDecode("대구","대구"));
    codes.add(new CodeDecode("울산","울산"));
    return codes;
  }




  // 회원가입양식
  @GetMapping("/add")
  public String joinForm(Model model) {
    List<CodeDecode> codes = new ArrayList<>();
    model.addAttribute("joinForm", new JoinForm());
    return "member/joinForm";
  }

  // 회원가입처리
  @PostMapping("/add")
  public String join(@Valid  @ModelAttribute JoinForm joinForm, BindingResult bindingResult) {
    log.info("joinForm={}", joinForm);
    if(bindingResult.hasErrors()) {
      log.info("bindingResult={}", bindingResult);
      return "member/joinForm";
    }

    // 비밀번호 체크
    if(!joinForm.getPasswd().equals(joinForm.getPasswdchk())) {
      bindingResult.reject("passwd","비번 다르잖아 ㅡㅡ");
      log.info("bindingResult={}", bindingResult);
      return "member/joinForm";
    }

    Member member = new Member();
    member.setEmail(joinForm.getEmail());
    member.setPasswd(joinForm.getPasswd());
    member.setNickname(joinForm.getNickname());
    member.setGender(joinForm.getGender());
    member.setHobby(hobbyToString(joinForm.getHobby()));
    member.setRegion(joinForm.getRegion());

    memberSVC.save(member);
    return "member/joinSuccess";
  }

  private String hobbyToString(List<String> hobby) {
    return StringUtils.join(hobby,",");
  }
}
