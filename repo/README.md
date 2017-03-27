Maven 本地仓库
==========

外部单独的 `jar` 通过本地仓库的形式进行管理。

安装
```
mvn install:install-file -Dfile=<path-to-file> -DgroupId=<myGroup> \ 
                         -DartifactId=<myArtifactId> -Dversion=<myVersion> \
                         -Dpackaging=<myPackaging> -DlocalRepositoryPath=<path>
```

例如
```
mvn install:install-file -Dfile=../judge/lib/AXMLPrinter2.jar  -DgroupId=android -DartifactId=axmlprinter -Dversion=2.0.0 -Dpackaging=jar -DlocalRepositoryPath=. 
```
