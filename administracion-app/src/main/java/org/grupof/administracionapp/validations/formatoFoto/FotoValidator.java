<<<<<<<< HEAD:administracion-app/src/main/java/org/grupof/administracionapp/validations/archivo/FotoValidator.java
package org.grupof.administracionapp.validations.archivo;
========
package org.grupof.administracionapp.validations.formatoFoto;
>>>>>>>> b3478ef7d51786e245b5f796ef6079acf741a22c:administracion-app/src/main/java/org/grupof/administracionapp/validations/formatoFoto/FotoValidator.java

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.web.multipart.MultipartFile;

public class FotoValidator implements ConstraintValidator<FotoTipoTamano, MultipartFile> {

    @Override
    public boolean isValid(MultipartFile file, ConstraintValidatorContext context) {
        if (file == null || file.isEmpty()) {
            return false;
        }

        String contentType = file.getContentType();
        long size = file.getSize();

        return (("image/png".equals(contentType) || "image/gif".equals(contentType)) && size <= 200 * 1024);
    }
}