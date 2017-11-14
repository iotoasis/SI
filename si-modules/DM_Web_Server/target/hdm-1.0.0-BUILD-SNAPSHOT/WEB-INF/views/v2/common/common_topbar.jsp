<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

            <nav class="navbar navbar-static-top navy-bg" role="navigation" style="margin-bottom: 0">
                <div class="navbar-header">
                    <a class="navbar-minimalize minimalize-styl-2 btn btn-primary " href="#"><i class="fa fa-bars"></i> </a>
                </div>
                <ul class="nav navbar-top-links navbar-right">
					<li>
						<h3><img src="${pageContext.request.contextPath}/images/hitdm/common/logo_oasis_m.png" width="82" height="30" /> Welcome to OASIS Administrator!!!</h3>
					</li>
                    <li>
                        <a href="${pageContext.request.contextPath}/security/logout.do">
                            <i class="fa fa-sign-out"></i> Log out
                        </a>
                    </li>
                </ul>

            </nav>