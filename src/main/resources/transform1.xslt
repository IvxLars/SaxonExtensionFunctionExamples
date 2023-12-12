<xsl:stylesheet version="2.0"
                xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
                xmlns:mf="http://lars.com/"
                >

    <xsl:template match="/">
        <!-- Using the custom extension function -->

        <xsl:variable name="num" select="doc/number/name" />

        <!-- Call the external Java method with the extracted name -->
        <xsl:value-of select="mf:sqrt($num)" />
    </xsl:template>

</xsl:stylesheet>