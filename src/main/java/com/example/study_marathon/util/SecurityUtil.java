package com.example.study_marathon.util;

import com.example.study_marathon.security.AppUserDetails;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Optional;

/**
 * セキュリティ関連のユーティリティクラス
 */
public class SecurityUtil {

    private SecurityUtil() {
        // インスタンス化禁止
    }

    /**
     * 現在ログインしているユーザーの AppUserDetails を取得する
     * @return ログイン中の場合 AppUserDetails, 未ログインの場合 empty
     */
    public static Optional<AppUserDetails> getCurrentUserDetails() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof AppUserDetails) {
            return Optional.of((AppUserDetails) authentication.getPrincipal());
        }
        return Optional.empty();
    }

    /**
     * 現在ログインしているユーザーの ID (Users.id) を取得する
     * @return ユーザーID (未ログイン時は null)
     */
    public static Long getCurrentUserId() {
        return getCurrentUserDetails()
                .map(details -> details.getUser().getId())
                .orElse(null);
    }
}
