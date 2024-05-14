  
from tkinter import *
from tkinter import messagebox
import pyqrcode
import os

window = Tk()
window.title("QR Code Generator")

#coed generation
def generate():
	if len(subject.get()) != 0:
		global myQr
		myQr= pyqrcode.create(subject.get())
		qrImage= myQr.xbm(scale=6)
		global photo
		photo = BitmapImage(data= qrImage)
	else:
		messagebox.showinfo("Error!", "Please Enter The Subject")
	try:
		showCode()
	except:
		pass
		
#code showing
def showCode():
	global photo
	notificationLabel.config(image= photo)
	subLabel.config(text= "Showing QR Code for: "+subject.get())
	

lab1 = Label(window, text="Enter Subject",  font=("Helvetica", 12))
lab1.grid(row=0, column= 0, sticky= N+S+E+W)

subject= StringVar()
subjectEntry = Entry(window, textvariable = subject,font=("Helvetica", 12))
subjectEntry.grid(row=0, column=1, sticky= N+S+E+W)

createButton = Button(window, text= "Create QR Code", font=("Helvetica", 12), width= 15,command= generate)
createButton.grid(row=0, column=3, sticky= N+S+E+W)

notificationLabel= Label(window)
notificationLabel.grid(row= 2, column=1, sticky= N+S+E+W)

subLabel= Label(window, text="")
subLabel.grid(row= 3, column=1, sticky= N+S+E+W)



#Making responsive layout:
totalRows= 3
totalCols = 3

for row in range(totalRows+1):
	window.grid_rowconfigure(row, weight=1)

for col in range(totalCols+1):
	window.grid_columnconfigure(col, weight=1)

#looping the GUI
window.mainloop()
