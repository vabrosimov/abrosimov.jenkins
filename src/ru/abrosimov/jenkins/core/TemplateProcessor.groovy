package ru.abrosimov.jenkins.core

class TemplateProcessor {

    static String process(
            Object jenkins,
            String templatePath,
            Map<String, Object> model,
            String outputPath
    ) {
        String text = jenkins.readFile(templatePath)

        model.each { k, v ->
            text = text.replace("\${${k}}", v.toString())
        }

        jenkins.writeFile(
                file: outputPath,
                text: text
        )

        return outputPath
    }
}
