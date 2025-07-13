package sba301.java.opentalk.utils;

import sba301.java.opentalk.dto.CompanyBranchDTO;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

public class PracticeReflection {
    public static void main(String[] args) throws IllegalAccessException, NoSuchMethodException, InvocationTargetException {
        CompanyBranchDTO companyBranchDTO = new CompanyBranchDTO();
        Class<?> companyBranchDTOClass = companyBranchDTO.getClass();

        System.out.println("Name: " + companyBranchDTO.getName());
        System.out.println("Simple Name: " + companyBranchDTOClass.getSimpleName());

        Package pkg = companyBranchDTOClass.getPackage();
        System.out.println("Package: " + pkg.getName());

        int modifiers = companyBranchDTOClass.getModifiers();
        System.out.println("Modifiers: " + modifiers);

        boolean isPublic = Modifier.isPublic(modifiers);
        boolean isProtected = Modifier.isProtected(modifiers);
        boolean isPrivate = Modifier.isPrivate(modifiers);
        System.out.println("isPublic: " + isPublic);
        System.out.println("isProtected: " + isProtected);
        System.out.println("isPrivate: " + isPrivate);

        Field[] fields = companyBranchDTOClass.getDeclaredFields();
        for (Field field : fields) {
            System.out.println("Field: " + field.getName());
        }
        fields[0].setAccessible(true);
        fields[0].set(companyBranchDTO, "Abc");
        System.out.println("Field name of Object: " + companyBranchDTO.getName());
        System.out.println("=========================================");

        Method[] methods = companyBranchDTOClass.getDeclaredMethods();
        for (Method method : methods) {
            System.out.println("Method: " + method.getName());
            int modifier = method.getModifiers();
            isPublic = Modifier.isPublic(modifier);
            isProtected = Modifier.isProtected(modifier);
            isPrivate = Modifier.isPrivate(modifier);
            System.out.println("isPublic: " + isPublic);
            System.out.println("isProtected: " + isProtected);
            System.out.println("isPrivate: " + isPrivate);
            System.out.println("=========================================");
        }

        Method private_identify = companyBranchDTOClass.getDeclaredMethod("identify", int.class);
        private_identify.setAccessible(true);
        System.out.println(private_identify.invoke(companyBranchDTO, 12));;
    }
}
