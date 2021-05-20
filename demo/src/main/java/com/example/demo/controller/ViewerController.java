package com.example.demo.controller;
 
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.regex.Pattern;

import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.thymeleaf.util.StringUtils;
 
@Controller
public class ViewerController {
	@Autowired
	protected ResourceLoader resourceLoader;

    @RequestMapping("/viewer")
	public String viewer(Model model, @RequestParam(name = "img", required = false) String img) throws IOException {
		if (StringUtils.isEmpty(img)) {
			return "demo_viewer";
		}
		String pathAndFile = "public/" + img + ".png";
		Resource resource = resourceLoader.getResource("classpath:" + pathAndFile);
		if (resource.exists()) {
			File file = resource.getFile();

			// ファイルが指定ディレクトリ下のものか確認する
			String confirmationPath = file.getCanonicalPath();
			// 正規表現がうまく行かない…
			// https://regex-testdrive.com/ja/dotest で確認したところOKだったのですが・・・
			boolean check = confirmationPath.matches("^.*public\\.*png$");
			if (!check) {
				return "demo_viewer";
			}
			
			Path path = file.toPath();
			byte[] bytes = Files.readAllBytes(path);
			String base64 = new String (Base64.encodeBase64(bytes), "ASCII");
			StringBuilder data = new StringBuilder();
			data.append("data:image/png;base64,");
			data.append(base64);
			model.addAttribute("path", data.toString());
		}
		return "demo_viewer";
	}
}