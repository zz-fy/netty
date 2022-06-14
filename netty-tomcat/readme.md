要定义一个 Tomcat，这个 Web 容器提供给用户后，用户只需要按照使用步骤
就可以将其自定义的 Servlet 发布到该 Tomcat 中。我们现在给出用户对于该 Tomcat 的使用
步骤：
 用户只需将自定义的 Servlet 放入到指定的包中。例如，com.abc.webapp 包中。
 用户在访问时，需要将自定义的 Servnet 的简单类名全小写后的字符串作为该 Servnet
的 Name 进行访问。
 若没有指定的 Servlet，则访问默认的 Servlet。
真正的 Tomcat：
第一个 map：key 为指定的 Servlet 的名称，value 为该 Servlet 实例
第二个 map：key 为指定的 Servlet 的名称，value 为该 Servlet 的全限定性类名