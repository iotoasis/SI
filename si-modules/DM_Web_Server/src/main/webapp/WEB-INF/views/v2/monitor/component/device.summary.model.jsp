<%@page pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>


					<div class="ibox float-e-margins">
						<div class="ibox-title">
							<h3 class="no-margins">모델정보</h3>
						</div>
						<div class="ibox-content" style="height: 135px;">
							<div class="row">
								<div class="col-lg-5">
									<img id="dsm_model_image" alt="디바이스 모델 이미지" class="img-responsive" src="${pageContext.request.contextPath}/images/hitdm/model/gw_01.jpg" style="width: 130px; height: 110px; margin-top: -10px;"/>
								</div>
								<div class="col-lg-7" style="margin-top: 5px; border:line-height: 20px; padding-left:0px;">
									<div>제조사</div>
									<div class="div-text-indent font-bold"><span id="dsm_model_manufacturer">-</span>(<span id="dsm_model_oui">-</span>)</div>
									<div style="height: 6px;"></div>
									<div>모델번호</div>
									<div class="div-text-indent font-bold" id="dsm_model_name"></div>
								</div>
							</div>
						</div>
					</div>