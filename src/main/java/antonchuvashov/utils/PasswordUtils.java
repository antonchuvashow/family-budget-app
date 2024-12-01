package antonchuvashov.utils;

import at.favre.lib.crypto.bcrypt.BCrypt;

public class PasswordUtils {

    // Хэширование пароля
    public static String hashPassword(String password) {
        return BCrypt.withDefaults().hashToString(12, password.toCharArray());
    }

    // Проверка пароля
    public static boolean checkPassword(String password, String storedHash) {
        BCrypt.Result result = BCrypt.verifyer().verify(password.toCharArray(), storedHash);
        return result.verified;
    }
}
