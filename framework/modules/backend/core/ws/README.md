Appverse Web - Web Services 
===========================
## Web Service Client - JAX-WS - AbstractWSClient 
The Appverse WS module provides an abstract class that will perform all the necessary code to connect to the web service. 

  public abstract class AbstractWSClient <S extends Service, I> implements IWSClient<I>

The abstract class will provide a `getService()` method that will connect to the remote service. 
This method performs the `getPort()` call on the first method call, then it will catch the service instance on the client side on the subsequent calls. 
That means the client code can decide to call the `getService()` method on the `@PostConstruct` event or on the first request.   

## Handlers
The framework provides two SOAP Handlers:

* ClientPerformanceMonitorLogicalHandler: Logs the time spent by the request.
* EnvelopeLoggingSOAPHandler: Logs the inbound / outbound SOAP messages.

Register the framework Handlers or custom Handlers implementations with the registerHandler method. 

     public MyServiceWSClient () { 
   		 this.setBeanClasses(MyServiceImpl.class, MyService.class); 
    	 this.registerHandler(new ClientPerformanceMonitorLogicalHandler()); 
    	 this.registerHandler(new EnvelopeLoggingSOAPHandler()); 
     } 
    
## More Information

* **About this project**: <http://appverse.github.com/appverse-web>
* **About licenses**: <http://www.mozilla.org/MPL/>
* **About The Appverse Project**: <http://appverse.org>

## License

    Copyright (c) 2012 GFT Appverse, S.L., Sociedad Unipersonal.

     This Source Code Form is subject to the terms of the Mozilla Public
     License, v. 2.0. If a copy of the MPL was not distributed with this
     file, You can obtain one at http://mozilla.org/MPL/2.0/.

     Redistribution and use in  source and binary forms, with or without modification, 
     are permitted provided that the  conditions  of the  Mozilla Public License v2.0 
     are met.

     THIS SOFTWARE IS PROVIDED BY THE  COPYRIGHT HOLDERS  AND CONTRIBUTORS "AS IS" AND
     ANY EXPRESS  OR IMPLIED WARRANTIES, INCLUDING, BUT  NOT LIMITED TO,   THE IMPLIED
     WARRANTIES   OF  MERCHANTABILITY   AND   FITNESS   FOR A PARTICULAR  PURPOSE  ARE
     DISCLAIMED. EXCEPT IN CASE OF WILLFUL MISCONDUCT OR GROSS NEGLIGENCE, IN NO EVENT
     SHALL THE  COPYRIGHT OWNER  OR  CONTRIBUTORS  BE LIABLE FOR ANY DIRECT, INDIRECT,
     INCIDENTAL,  SPECIAL,   EXEMPLARY,  OR CONSEQUENTIAL DAMAGES  (INCLUDING, BUT NOT
     LIMITED TO,  PROCUREMENT OF SUBSTITUTE  GOODS OR SERVICES;  LOSS OF USE, DATA, OR
     PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY,
     WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT(INCLUDING NEGLIGENCE OR OTHERWISE) 
     ARISING  IN  ANY WAY OUT  OF THE USE  OF THIS  SOFTWARE,  EVEN  IF ADVISED OF THE 
     POSSIBILITY OF SUCH DAMAGE.
