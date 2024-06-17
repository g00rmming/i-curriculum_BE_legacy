package goorming.iCurriculum.login.service;

import goorming.iCurriculum.login.dto.UserDetailsDTO;
import goorming.iCurriculum.member.Member;
import goorming.iCurriculum.member.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class CustomUserDetailService implements UserDetailsService {

    private final MemberRepository memberRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Member member = memberRepository.findByClientId(username);

        if (member != null) {
            return new UserDetailsDTO(member);
        }

        return null;
    }
}
