pipeline {
	agent any
	
	tools {
		maven 'maven-3.9.11'
	}
	
	stages {
		stage('Checkout'){
			steps{
				git branch : 'main', url :'https://github.com/SaranyaMadireddy/Selenium-Test-Framework.git'
			}
		}
		
		stage('Build'){
			steps{
				bat 'mvn clean install'
			}
		}
		
		stage('Test'){
			steps{
				bat 'mvn test'
			}
		}
		
		stage('Reports'){
			steps{
				publishHTML(target:[
					reportDir:'src/test/resources/extentreport',
					reportFiles:'ExtentReport.html',
					reportName: 'Extent Spark Report'
					
				])
			}
		}
		
	
	
	post {
		always{
			archiveArtifacts artifacts:'**/src/test/resources/extentreport/*.html',fingerprint: true
			junit 'target/surfire-reports/*.xml'
		}
	}
	
	success {
		emailext (
			to: 'saranya.madireddy@gmail.com',
			subject: "Build Success: ${env.JOB_Name} #${env.BUILD_NUMBER}",
			body: """
			<html>
			<body>
			<p>Hello Team,</p>
			
			<p>The Latest Jenkins Build has completed.</p>
			
			<p><b>Project Name:</b> ${env.JOB_NAME}</p>	
			<p><b>Build Number:</b> ${env.BUILD_NUMBER}</p>	
			<p><b>Build Status:</b> <span style="color: green;"><b>SUCCESS</b></span></p>	
			<p><b>Build URL:</b> <a href="${env.BUILD_URL}">${env.BUILD_URL}</a></p>
			
			<p><b>Last Commit:</b></p>
			<p>${env.GIT_COMMIT}</p>
			<p><b>Branch:</b> ${env.GIT_BRANCH}</p>
			
			<p><b>Build log is attached.</b></p>
			
			<p><b>Extent Report:</b> <a href="http://localhost:8080/job/OrangeHRM_Build/HTML_20Extent_20Report/">Click here</a></p>
			
			<p>Best regards,</p>
			<p><b>Automation Team</b></p>
			</body>
			</html>
			""",
			mimeType: 'text/html',
			attachLog: true
						
		)
	}
	
	failure {
		emailext (
			to: 'saranya.madireddy@gmail.com',
			subject: "Build Success: ${env.JOB_Name} #${env.BUILD_NUMBER}",
			body: """
			<html>
			<body>
			<p>Hello Team,</p>
			
			<p>The Latest Jenkins Build has <b style="color: red;">FAILED</b>.</p>
			
			<p><b>Project Name:</b> ${env.JOB_NAME}</p>	
			<p><b>Build Number:</b> ${env.BUILD_NUMBER}</p>	
			<p><b>Build Status:</b> <span style="color: red;"><b>FAILED &#10060;</b></span></p>	
			<p><b>Build URL:</b> <a href="${env.BUILD_URL}">${env.BUILD_URL}</a></p>
			
			<p><b>Last Commit:</b></p>
			<p>${env.GIT_COMMIT}</p>
			<p><b>Branch:</b> ${env.GIT_BRANCH}</p>
			
			<p><b>Build log is attached.</b></p>
			
			<p><b>Extent Report:</b> <a href="http://localhost:8080/job/OrangeHRM_Build/HTML_20Extent_20Report/">Click here</a></p>
			
			<p>Best regards,</p>
			<p><b>Automation Team</b></p>
			</body>
			</html>
			""",
			mimeType: 'text/html',
			attachLog: true
						
		)
	}
	}		
}