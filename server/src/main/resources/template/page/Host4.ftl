<#--

    ActivityInfo
    Copyright (C) 2009-2013 UNICEF
    Copyright (C) 2014-2018 BeDataDriven Groep B.V.

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program.  If not, see <http://www.gnu.org/licenses/>.

-->
<!DOCTYPE html>
<#-- @ftlvariable name="" type="org.activityinfo.server.login.model.HostPageModel" -->
<#if appCacheManifest??>
<html manifest="${appCacheManifest}">
<#else>
<html>
</#if>
<head>
    <meta http-equiv="content-type" content="text/html; charset=UTF-8">
    <meta name="application-name" content="ActivityInfo"/>
    <meta name="description" content="ActivityInfo"/>
    <meta name="application-url" content="${appUrl}"/>
    <meta http-equiv="X-UA-Compatible" content="IE=10">
    <meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0">

    <link rel="icon" href="/about/assets/images/logo-activityinfo.png">
    <link rel="manifest" href="/activityinfo.webmanifest">
    <link rel="stylesheet" href="https://fonts.typotheque.com/WF-028542-010274.css" type="text/css" />

    <title>${domain.title}</title>

    <style type="text/css">

        #initial-loader {
            position: absolute;
            top: 0;
            left: 0;
            right: 0;
            bottom: 0;
            display: flex;
            justify-content: center;
            align-items: center;
            background-color: #F2F7F7;
            z-index: -2;
        }

        .loader {
            position: relative;
            display: flex;
            justify-content: center;
            align-items: center;
            width: 10rem;
            height: 10rem;
        }
        .loader:before {
            z-index: -1;
            content: "";
            position: absolute;
            top: 50%;
            left: 50%;
            width: 100%;
            height: 100%;
            border-radius: 50% 50%;
            background-color: #FFF;
            -webkit-transform: translate(-50%, -50%);
            transform: translate(-50%, -50%);
        }

        .loader__blob {
            display: block;
            width: 1rem;
            height: 1rem;
            margin: 0.5rem;
            border-radius: 50% 50%;
            -webkit-transform-origin: 50% 50%;
            transform-origin: 50% 50%;
            -webkit-animation-name: a-blobs;
            animation-name: a-blobs;
            -webkit-animation-duration: 1.6s;
            animation-duration: 1.6s;
            -webkit-animation-iteration-count: infinite;
            animation-iteration-count: infinite;
            -webkit-animation-direction: normal;
            animation-direction: normal;
            -webkit-animation-timing-function: linear;
            animation-timing-function: linear;
            -webkit-animation-fill-mode: forwards;
            animation-fill-mode: forwards;
        }
        .loader__blob:nth-child(1) {
            background-color: #00CF79;
        }
        .loader__blob:nth-child(2) {
            background-color: #000;
            -webkit-animation-delay: 0.2s;
            animation-delay: 0.2s;
        }
        .loader__blob:nth-child(3) {
            background-color: #D9D9D9;
            -webkit-animation-delay: 0.4s;
            animation-delay: 0.4s;
        }

        @-webkit-keyframes a-blobs {
            0%, 60%, 100% {
                -webkit-transform: translateY(0);
                transform: translateY(0);
            }
            20% {
                -webkit-transform: translateY(-60%);
                transform: translateY(-60%);
            }
            40% {
                -webkit-transform: translateY(60%);
                transform: translateY(60%);
            }
        }

        @keyframes a-blobs {
            0%, 60%, 100% {
                -webkit-transform: translateY(0);
                transform: translateY(0);
            }
            20% {
                -webkit-transform: translateY(-60%);
                transform: translateY(-60%);
            }
            40% {
                -webkit-transform: translateY(60%);
                transform: translateY(60%);
            }
        }

    </style>
    <script type="text/javascript">
        if (document.cookie.indexOf('authToken=') == -1 ||
                document.cookie.indexOf('userId') == -1 ||
                document.cookie.indexOf('email') == -1) {
            window.location = "/login" + window.location.hash;
        }
        var ClientContext = {
            version: '${buildProperties.version}',
            commitId: '${buildProperties.commitId}',
            title: '${domain.title}',
            featureFlags: '${featureFlags!''}'

        };
    </script>
    <script type="text/javascript" language="javascript" src="${bootstrapScript}"></script>
    <script>
        (function(i,s,o,g,r,a,m){i['GoogleAnalyticsObject']=r;i[r]=i[r]||function(){
                    (i[r].q=i[r].q||[]).push(arguments)},i[r].l=1*new Date();a=s.createElement(o),
                m=s.getElementsByTagName(o)[0];a.async=1;a.src=g;m.parentNode.insertBefore(a,m)
        })(window,document,'script','https://www.google-analytics.com/analytics.js','ga');
        ga('create', 'UA-11567120-1', 'auto');
        ga('set', 'anonymizeIp', true);
        ga('send', 'pageview');
    </script>
</head>
<body role="application" >
    <div id="initial-loader">
        <div class="loader">
            <div class="loader__blob"></div>
            <div class="loader__blob"></div>
            <div class="loader__blob"></div>
        </div>
    </div>
    <iframe src="javascript:''" id="__gwt_historyFrame" tabIndex='-1' style="position:absolute;width:0;height:0;border:0"></iframe>
</body>
</html>