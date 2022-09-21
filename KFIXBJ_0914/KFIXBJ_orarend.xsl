<?xml version="1.0" encoding="UTF-8"?>

<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">

<xsl:template match="/">
  <html>
  <head>
  <link rel="stylesheet" href="./main.css" />
  </head>
  <body>
  <h2>Órarend</h2>
  <table>
    <tr>
      <thead>
        <tr>
          <th>targy</th>
          <th>idopont</th>
          <th>helyszin</th>
          <th>oktato</th>
          <th>szak</th>
        </tr>
      </thead>
      <tbody>
        <xsl:for-each select="MB_orarend/ora">
        <tr>
          <td><xsl:value-of select="targy"/></td>
          <td><xsl:value-of select="idopont"/></td>
          <td><xsl:value-of select="helyszin"/></td>
          <td><xsl:value-of select="oktato"/></td>
          <td><xsl:value-of select="szak"/></td>
        </tr>
        </xsl:for-each>
      </tbody>
    </tr>
  </table>
  </body>
  </html>
</xsl:template>

</xsl:stylesheet> 
