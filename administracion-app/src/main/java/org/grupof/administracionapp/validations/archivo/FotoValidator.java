package org.grupof.administracionapp.validations.archivo;

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