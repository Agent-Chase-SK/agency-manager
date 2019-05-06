<%@page contentType="text/html;charset=utf-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<html>
<body>

<table border="1">
    <thead>
    <tr>
        <th>Code Name</th>
        <th>Status</th>
    </tr>
    </thead>
    <c:forEach items="${agents}" var="agent">
        <tr>
            <td><c:out value="${agent.codeName}"/></td>
            <td><c:out value="${agent.status}"/></td>
            <td><form method="post" action="${pageContext.request.contextPath}/AgentServlet/retire?id=${agent.id}"
                      style="margin-bottom: 0;"><input type="submit" value="Retire"></form></td>
        </tr>
    </c:forEach>
</table>

<h2>Create agent</h2>
<c:if test="${not empty formError}">
    <div style="border: solid 1px red; background-color: yellow; padding: 10px">
        <c:out value="${formError}"/>
    </div>
</c:if>
<form action="${pageContext.request.contextPath}/AgentServlet/create" method="post">
    <table>
        <tr>
            <th>Code name:</th>
            <td><input type="text" name="codeName" value="<c:out value='${param.codeName}'/>"/></td>
        </tr>
        <tr>
            <th>Status:</th>
            <td>
                <select name='status'>
                    <option value='ACTIVE'>Active</option>
                    <option value='RETIRED'>Retired</option>
                    <option value='KIA'>K.I.A.</option>
                    <option value='MIA'>M.I.A.</option>
                    <option value='TRAITOR'>Traitor</option>
                </select>
            </td>
        </tr>
    </table>
    <input type="Submit" value="Submit" />
</form>

</body>
</html>
