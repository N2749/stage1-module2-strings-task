package com.epam.mjc;

import java.util.Arrays;
import java.util.List;

public class MethodParser {

    private static final int ARGUMENT_START_INDEX_WITH_ACCESS_MODIFIER = 3;
    private static final int ARGUMENT_START_INDEX_WITHOUT_ACCESS_MODIFIER = 2;
    private static final int RETURN_TYPE_INDEX_WITH_ACCESS_MODIFIER = 1;
    private static final int RETURN_TYPE_INDEX_WITHOUT_ACCESS_MODIFIER = 0;
    private static final int METHOD_NAME_INDEX_WITH_ACCESS_MODIFIER = 2;
    private static final int METHOD_NAME_INDEX_WITHOUT_ACCESS_MODIFIER = 1;
    private static final int ACCESS_MODIFIER_INDEX = 0;

    /**
     * Parses string that represents a method signature and stores all it's members into a {@link MethodSignature} object.
     * signatureString is a java-like method signature with following parts:
     * 1. access modifier - optional, followed by space: ' '
     * 2. return type - followed by space: ' '
     * 3. method name
     * 4. arguments - surrounded with braces: '()' and separated by commas: ','
     * Each argument consists of argument type and argument name, separated by space: ' '.
     * Examples:
     * accessModifier returnType methodName(argumentType1 argumentName1, argumentType2 argumentName2)
     * private void log(String value)
     * Vector3 distort(int x, int y, int z, float magnitude)
     * public DateTime getCurrentDateTime()
     *
     * @param signatureString source string to parse
     * @return {@link MethodSignature} object filled with parsed values from source string
     */
    public MethodSignature parseFunction(String signatureString) {
        StringSplitter stringSplitter = new StringSplitter();
        List<String> signatureList = stringSplitter.splitByDelimiters(signatureString, Arrays.asList(" ", "(", ", ", ")"));
        if (Arrays.asList("public", "protected", "private").contains(signatureList.get(0))) {
            return withModifier(signatureList);
        }
        return withoutModifier(signatureList);
    }

    private MethodSignature withoutModifier(List<String> signatureList) {
        MethodSignature methodSignature = new MethodSignature(signatureList.get(METHOD_NAME_INDEX_WITHOUT_ACCESS_MODIFIER));
        return setArguments(signatureList, methodSignature, RETURN_TYPE_INDEX_WITHOUT_ACCESS_MODIFIER, ARGUMENT_START_INDEX_WITHOUT_ACCESS_MODIFIER);
    }

    private MethodSignature withModifier(List<String> signatureList) {
        MethodSignature methodSignature = new MethodSignature(signatureList.get(METHOD_NAME_INDEX_WITH_ACCESS_MODIFIER));
        methodSignature.setAccessModifier(signatureList.get(ACCESS_MODIFIER_INDEX));
        return setArguments(signatureList, methodSignature, RETURN_TYPE_INDEX_WITH_ACCESS_MODIFIER, ARGUMENT_START_INDEX_WITH_ACCESS_MODIFIER);
    }

    private MethodSignature setArguments(List<String> signatureList, MethodSignature methodSignature, int returnTypeIndex, int argumentStartIndex) {
        methodSignature.setReturnType(signatureList.get(returnTypeIndex));
        for (int i = argumentStartIndex; i <= signatureList.size() - 2; i += 2) {
            methodSignature
                    .getArguments()
                    .add(
                            new MethodSignature
                                    .Argument(
                                    signatureList.get(i),
                                    signatureList.get(i + 1)
                            )
                    );
        }
        return methodSignature;
    }

    public static void main(String[] args) {
        MethodParser parser = new MethodParser();
        parser.parseFunction("String repeat(String value, int times)");
    }
}
