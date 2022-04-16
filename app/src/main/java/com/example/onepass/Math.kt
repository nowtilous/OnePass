package onepass

import java.nio.charset.StandardCharsets.UTF_8
import java.security.MessageDigest
import java.util.Base64


internal object Math {

    private const val HASH_ALGORITHM = "SHA-256"


    fun calculateHMAC(mainPassword: String, salt: String): String {

        // some websites don't allow special characters
        return base64Hash("$mainPassword:$salt").subSequence(0, 20).toString()
                        .replace('/','a')
                        .replace('+','E');
    }

    /**
     * @returns a base64-encoded hash of a string
     */
    private fun base64Hash(str: String): String {
        try {

            val data = str.toByteArray(UTF_8)
            val digester = MessageDigest.getInstance(HASH_ALGORITHM)
            digester.update(data)
            return Base64.getEncoder().encodeToString(digester.digest())
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return ""
    }
}
