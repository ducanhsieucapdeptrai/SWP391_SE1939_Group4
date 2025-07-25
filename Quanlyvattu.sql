CREATE DATABASE quan_ly_vat_tu;
USE quan_ly_vat_tu;

CREATE TABLE Roles (
    RoleId INT PRIMARY KEY,
    RoleName VARCHAR(100) NOT NULL
);
INSERT INTO Roles (RoleId, RoleName)
VALUES
(1, 'Warehouse Manager'),
(2, 'Warehouse Staff'),
(3, 'Director'),
(4, 'Accountant'),
(5, 'Company Staff')
;




CREATE TABLE Modules (
    ModuleId INT AUTO_INCREMENT PRIMARY KEY,
    ModuleName VARCHAR(100) NOT NULL,
    Description VARCHAR(255) NULL
);
INSERT INTO Modules (ModuleName)
VALUES
  ('User Management'),
  ('Material Management'),
  ('Import/Export Materials'),
  ('Material Requests'),
  ('Statistics & Reports');
  
  
  

CREATE TABLE Functions (
    FunctionId INT AUTO_INCREMENT PRIMARY KEY,
    FunctionName VARCHAR(100) NOT NULL,
    Url VARCHAR(255) NULL, -- Made NULLABLE to allow initial inserts without URL
    ModuleId INT NULL,    -- Made NULLABLE to allow initial inserts without ModuleId
    FOREIGN KEY (ModuleId) REFERENCES Modules(ModuleId)
);

-- Module 1: User Management
INSERT INTO Functions (FunctionName, Url, ModuleId) VALUES
('User List', '/userlist', 1),
('Reset Password Requests', '/reset-pass-list', 1),
('Authorization Matrix', '/user-matrix', 1),
('Request password reset', '/request-new-password', 1),
('Assign user roles', NULL, 1),
('View Notification', '/all-notifications', 1),
('Add User', '/add-user', 1);




-- Module 2: Material Management
INSERT INTO Functions (FunctionName, Url, ModuleId) VALUES
('Material Inventory', '/materiallist', 2),
('Add new material', '/material-add' , 2),
('Edit material information', '/materialdetail', 2),
('Add Category/Subcategory', '/add-category' , 2),
('Delete material', NULL, 2),
('Edit Material Information', '/editmaterial', 2),
('Inventory Check', '/inventory-check', 2);


-- Module 3: Import/Export
INSERT INTO Functions (FunctionName, Url, ModuleId) VALUES
('Create Request', '/createrequest', 3),
('Warehouse Report', '/warehousereport', 3);

-- Module 4: Requests
INSERT INTO Functions (FunctionName, Url, ModuleId) VALUES
('Request List', '/reqlist', 4),
('Purchase Request List', '/purchase-request-list', 4),
('Repair Order List', '/repair-request-list', 4),
('My Requests', '/my-request', 4),
('Task List', '/tasklist', 4),
('Completed Tasks', '/completedTasks', 4),
('View Request Detail', '/request-detail', 4),
('View Purchase Order Detail', '/purchase-order-detail', 4),
('View Repair Order Detail', '/repair-order-detail', 4),
('Approve Request Form', '/approve-request', 4),
('Reject Request', '/approveandrejectrequest', 4),
('Approve Request', '/approveandrejectrequest', 4),
('Create PO button', '/create-purchase-order', 4),
('Create PO', '/create-po', 4),
('View Project', '/project', 4)





;
-- Module 5: Reports
INSERT INTO Functions (FunctionName, Url, ModuleId) VALUES
('Advanced Dashboard', '/advanced-dashboard', 5),
('Dashboard', '/dashboard', 5),
('View Statistic', '/material-statistics', 5);






CREATE TABLE RoleFunction (
    RoleId INT NOT NULL,
    FunctionId INT NOT NULL,
    IsActive TINYINT(1) NOT NULL DEFAULT 1,
    PRIMARY KEY (RoleId, FunctionId),
    FOREIGN KEY (RoleId) REFERENCES Roles(RoleId),
    FOREIGN KEY (FunctionId) REFERENCES Functions(FunctionId)
);


-- Gán tất cả quyền cho Warehouse Manager
INSERT INTO RoleFunction (RoleId, FunctionId)
SELECT 1, FunctionId FROM Functions;

-- Gán các quyền chính cho Warehouse Staff
INSERT INTO RoleFunction (RoleId, FunctionId)
SELECT 2, FunctionId FROM Functions WHERE FunctionName IN (
    'Material Inventory', 'Create Request', 'Warehouse Report',
    'My Requests', 'Task List', 'Dashboard'
);

-- Gán quyền cho Director
INSERT INTO RoleFunction (RoleId, FunctionId)
SELECT 3, FunctionId FROM Functions WHERE FunctionName IN (
    'Material Inventory', 'Request List', 'Purchase Request List',
    'Repair Order List', 'My Requests', 'Completed Tasks',
    'Advanced Dashboard', 'Dashboard','User List','Approve Request','View Repair Order Detail','View Request Detail','View Purchase Order Detail','View Request Detail'
    'Create PO','Approve Request Form','View Project'
);

-- Gán quyền cho Company Staff
INSERT INTO RoleFunction (RoleId, FunctionId)
SELECT 4, FunctionId FROM Functions WHERE FunctionName IN (
    'Material Inventory', 'Create Request', 'My Requests', 'Dashboard'
);





CREATE TABLE Users (
    UserId INT PRIMARY KEY AUTO_INCREMENT,
    FullName VARCHAR(100),
    UserImage VARCHAR(100),
    Email VARCHAR(100) UNIQUE,
    Phone VARCHAR(20),
    Password VARCHAR(100),
    RoleId INT,
    IsActive BOOLEAN,
    FOREIGN KEY (RoleId) REFERENCES Roles(RoleId)
);

ALTER TABLE Users
  ADD COLUMN DateOfBirth DATE NULL,
  ADD COLUMN Gender VARCHAR(10) NULL,
  ADD COLUMN Address VARCHAR(255) NULL;

INSERT INTO Users (FullName, UserImage, Email, Phone, Password, RoleId, IsActive)
VALUES
('Trần Quản Lý', 'warehousemanager.png','quanlyvattu4@gmail.com', '0900000000', 'quanly123', 1, TRUE),
('Nguyễn Giám Đốc',  'director.png','giamdoc@example.com', '0911111111', 'giamdoc123', 3, TRUE),
('Nguyễn Văn A', 'warehousestaff.png', 'a1@example.com', '0900000001', 'pass1', 2, TRUE),
('Nguyễn Văn B',  'warehousestaff.png','a2@example.com', '0900000002', 'pass2', 2, TRUE),
('Nguyễn Nhân Viên', 'companystaff.png', 'nhanvienkho8686@gmail.com', '0912345678', 'nhanvien123', 2, TRUE),
('Ngô Thị I', 'companystaff.png', 'b1@example.com', '0900000011', 'pass11', 4, TRUE);



-- Update user details
UPDATE Users SET DateOfBirth = '1978-04-12', Gender = 'Nam', Address = 'Hà Nội' WHERE UserId = 1;
UPDATE Users SET DateOfBirth = '1975-09-30', Gender = 'Nam', Address = 'Hồ Chí Minh' WHERE UserId = 2;
UPDATE Users SET DateOfBirth = '1990-01-15', Gender = 'Nam', Address = 'Đà Nẵng' WHERE UserId = 3;
UPDATE Users SET DateOfBirth = '1988-03-22', Gender = 'Nam', Address = 'Hải Phòng' WHERE UserId = 4;
UPDATE Users SET DateOfBirth = '1992-07-05', Gender = 'Nam', Address = 'Cần Thơ' WHERE UserId = 5;
UPDATE Users SET DateOfBirth = '1989-11-11', Gender = 'Nữ', Address = 'Bình Dương' WHERE UserId = 6;



CREATE TABLE Categories (
    CategoryId INT AUTO_INCREMENT PRIMARY KEY,
    CategoryName VARCHAR(255) NOT NULL
);
INSERT INTO Categories (CategoryName)
VALUES
('Structural Materials'),
('Construction Machinery'),
('Safety Equipment');


CREATE TABLE SubCategories (
    SubCategoryId INT AUTO_INCREMENT PRIMARY KEY,
    SubCategoryName VARCHAR(255) NOT NULL,
    CategoryId INT,
    FOREIGN KEY (CategoryId) REFERENCES Categories(CategoryId)
);

INSERT INTO SubCategories (SubCategoryName, CategoryId)
VALUES
('Concrete', 1), -- 1
('Structural Steel', 1),-- 2
('Bricks', 1),-- 3
('Concrete Machinery', 2),-- 4
('Earthmoving Equipment', 2),-- 5
('Lifting Equipment', 2),-- 6
('Body Protection', 3),-- 7
('Fall Protection', 3),-- 8
('Respiratory Protection', 3);-- 9

CREATE TABLE MaterialStatus (
    StatusId INT AUTO_INCREMENT PRIMARY KEY,
    StatusName VARCHAR(100)
);

INSERT INTO MaterialStatus (StatusId, StatusName) -- Explicitly set StatusId for alignment
VALUES (1, 'Available'), (2, 'UnAvailable');

CREATE TABLE Materials (
    MaterialId INT AUTO_INCREMENT PRIMARY KEY,
    MaterialName VARCHAR(300),
    SubCategoryId INT,
    StatusId INT,
    Image VARCHAR(255),
    TotalQuantity INT DEFAULT 0,
    Description TEXT,
    Unit VARCHAR(50) DEFAULT 'unit',
    FOREIGN KEY (SubCategoryId) REFERENCES SubCategories(SubCategoryId),
    FOREIGN KEY (StatusId) REFERENCES MaterialStatus(StatusId)
);

INSERT INTO Materials (MaterialName, SubCategoryId, StatusId, Image, Description, TotalQuantity)
VALUES
('Reinforced Concrete', 1, 1, 'reinforced-concrete.png', 'High strength concrete with steel reinforcement', 100),
('Lightweight Concrete', 1, 1, 'lightweight-concrete.png', 'Concrete with low density for non-load-bearing walls', 50),
('I-Beam 200x100', 2, 1, 'i-beam-200x100.png', 'Standard structural steel I-beam', 200),
('H-Beam 300x300', 2, 1, 'h-beam-300x300.png', 'Heavy-duty H-beam', 150),
('Red Clay Brick', 3, 1, 'red-clay-brick.png', 'Traditional fired clay brick', 10000),
('Non-fired Brick', 3, 1, 'non-fired-brick.png', 'Eco-friendly construction brick', 8000),
('Granite Stone', 4, 1, 'granite-stone.png', 'Used for flooring and wall cladding', 300),
('Limestone Block', 4, 1, 'limestone-block.png', 'For foundation and support walls', 250),
('Pine Timber', 5, 1, 'pine-timber.png', 'Used in structural framing', 500),
('Oak Beam', 5, 1, 'oak-beam.png', 'Heavy-duty wood for decorative structure', 200),
('Ceramic Tile 60x60', 6, 1, 'ceramic-tile-60x60.png', 'Matte finish ceramic tile', 1000),
('Laminate Flooring', 6, 1, 'laminate-flooring.png', 'Wood-pattern laminate', 600),
('Wall Tile 30x60', 7, 1, 'wall-tile-30x60.png', 'Glossy finish wall tile', 800),
('Wallpaper Roll', 7, 1, 'wallpaper-roll.png', 'Modern style wallpaper', 300),
('Water-based Paint (White)', 8, 1, 'water-based-paint-white.png', 'High coverage, interior paint', 500),
('Exterior Paint (Blue)', 8, 1, 'exterior-paint-blue.png', 'Weather-resistant paint', 300),
('Gypsum Ceiling Board', 9, 1, 'gypsum-ceiling-board.png', 'Used for suspended ceilings', 400),
('Aluminum Ceiling Panel', 9, 1, 'aluminum-ceiling-panel.png', 'Waterproof, reflective surface', 200);





CREATE TABLE MaterialInventoryCheck (
    InventoryId INT AUTO_INCREMENT PRIMARY KEY,
    MaterialId INT,
    StatusId INT, -- 1: New, 2: Used, 3: Damaged
    Quantity INT DEFAULT 0,
    LastUpdated DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (MaterialId) REFERENCES Materials(MaterialId),
    FOREIGN KEY (StatusId) REFERENCES MaterialStatus(StatusId),
    UNIQUE (MaterialId, StatusId) -- Mỗi vật tư + trạng thái chỉ có 1 dòng
);





CREATE TABLE RequestType (
    RequestTypeId INT AUTO_INCREMENT PRIMARY KEY,
    RequestTypeName VARCHAR(100)
);

INSERT INTO RequestType (RequestTypeName)
VALUES
('Export for Repair'),
('Export for Construction'),

('Purchase'),


('Export for Repair'),
('Import from Repair'),
('Import New Purchase'),
('Import from Usage')

;



CREATE TABLE RequestStatus (
    StatusCode VARCHAR(20) PRIMARY KEY,
    Description VARCHAR(100)
);

INSERT INTO RequestStatus (StatusCode, Description)
VALUES
('Pending', 'Waiting for approval'),
('Approved', 'Approved by director'),
('Rejected', 'Request has been rejected'),
('Completed', 'Successfull');



CREATE TABLE RequestList (
    RequestId INT AUTO_INCREMENT PRIMARY KEY,
    RequestedBy INT,
    RequestDate DATETIME DEFAULT CURRENT_TIMESTAMP,
    RequestTypeId INT,
    Note TEXT,
    Status VARCHAR(20) DEFAULT 'Pending',
    ApprovedBy INT,
    ApprovedDate DATETIME,
    ApprovalNote TEXT,
    ArrivalDate DATETIME,
    IsTransferredToday BOOLEAN DEFAULT FALSE,
    IsUpdated BOOLEAN DEFAULT FALSE NOT NULL,
    FOREIGN KEY (RequestedBy) REFERENCES Users(UserId),
    FOREIGN KEY (RequestTypeId) REFERENCES RequestType(RequestTypeId),
    FOREIGN KEY (ApprovedBy) REFERENCES Users(UserId),
    FOREIGN KEY (Status) REFERENCES RequestStatus(StatusCode)
);




CREATE TABLE RequestDetail (
    RequestId INT,
    MaterialId INT,
    Quantity INT,
    ActualQuantity INT DEFAULT 0,
    PRIMARY KEY (RequestId, MaterialId),
    FOREIGN KEY (RequestId) REFERENCES RequestList(RequestId),
    FOREIGN KEY (MaterialId) REFERENCES Materials(MaterialId)
);





-- ========== NOTIFICATIONS ==========
CREATE TABLE NotificationTypes (
    TypeId INT AUTO_INCREMENT PRIMARY KEY,
    TypeName VARCHAR(255) NOT NULL
);
INSERT INTO NotificationTypes (TypeName) VALUES
('CATEGORY_ADDED'),
('SUBCATEGORY_ADDED'),
('MATERIAL_ADDED'),
('REQUEST_CREATED'),
('REQUEST_APPROVED'),
('REQUEST_REJECTED'),
('ORDER_CREATED'),
('ORDER_APPROVED'),
('ORDER_REJECTED'),
('TASK_COMPLETED'),
('TASK_STARTED'),
('SYSTEM');


CREATE TABLE Notifications (
    NotificationId INT AUTO_INCREMENT PRIMARY KEY,
    UserId INT,
    TypeId INT NOT NULL,
    Title VARCHAR(255),
    Message VARCHAR(255) NOT NULL,
    Url VARCHAR(255),
    RelatedId INT,
    RelatedType VARCHAR(50),
    Priority VARCHAR(20) DEFAULT 'MEDIUM',
    IsRead BOOLEAN DEFAULT FALSE,
    CreatedAt DATETIME DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (UserId) REFERENCES Users(UserId),
    FOREIGN KEY (TypeId) REFERENCES NotificationTypes(TypeId)
);




CREATE TABLE PurchaseOrderStatus (
    StatusCode VARCHAR(20) PRIMARY KEY,
    Description VARCHAR(100)
);

INSERT INTO PurchaseOrderStatus (StatusCode, Description) VALUES
('Pending', 'Waiting for director approval'),
('Approved', 'Approved by director'),
('Rejected', 'Rejected by director');



CREATE TABLE PurchaseOrderList (
    POId INT AUTO_INCREMENT PRIMARY KEY,
    RequestId INT NOT NULL,
    CreatedBy INT NOT NULL,
    CreatedDate DATETIME DEFAULT CURRENT_TIMESTAMP,
    TotalPrice DOUBLE,
    Status VARCHAR(20) DEFAULT 'Pending',
    ApprovedBy INT,
    ApprovedDate DATETIME,
    Note TEXT,
    FOREIGN KEY (RequestId) REFERENCES RequestList(RequestId),
    FOREIGN KEY (CreatedBy) REFERENCES Users(UserId),
    FOREIGN KEY (ApprovedBy) REFERENCES Users(UserId),
    FOREIGN KEY (Status) REFERENCES PurchaseOrderStatus(StatusCode)
);





CREATE TABLE PurchaseOrderDetail (
    POId INT,
    MaterialId INT,
    Quantity INT,
    UnitPrice DOUBLE,
    Total DOUBLE,
    PRIMARY KEY (POId, MaterialId),
    FOREIGN KEY (POId) REFERENCES PurchaseOrderList(POId),
    FOREIGN KEY (MaterialId) REFERENCES Materials(MaterialId)
);





-- Bảng lưu trạng thái phiếu sửa chữa
CREATE TABLE RepairOrderStatus (
    StatusCode VARCHAR(20) PRIMARY KEY,
    Description VARCHAR(100)
);

-- Dữ liệu mẫu cho trạng thái phiếu sửa chữa
INSERT INTO RepairOrderStatus (StatusCode, Description) VALUES
('Pending', 'Waiting for director approval'),
('Approved', 'Approved by director'),
('Rejected', 'Rejected by director');

-- Bảng danh sách phiếu sửa chữa
CREATE TABLE RepairOrderList (
    ROId INT AUTO_INCREMENT PRIMARY KEY,
    RequestId INT NOT NULL,
    CreatedBy INT NOT NULL,
    CreatedDate DATETIME DEFAULT CURRENT_TIMESTAMP,
    Status VARCHAR(20) DEFAULT 'Pending',
    ApprovedBy INT,
    ApprovedDate DATETIME,
    Note TEXT,
    FOREIGN KEY (RequestId) REFERENCES RequestList(RequestId),
    FOREIGN KEY (CreatedBy) REFERENCES Users(UserId),
    FOREIGN KEY (ApprovedBy) REFERENCES Users(UserId),
    FOREIGN KEY (Status) REFERENCES RepairOrderStatus(StatusCode)
);

-- Bảng chi tiết phiếu sửa chữa
CREATE TABLE RepairOrderDetail (
    ROId INT,
    MaterialId INT,
    UnitPrice DOUBLE,
    MNote TEXT,
    PRIMARY KEY (ROId, MaterialId),
    FOREIGN KEY (ROId) REFERENCES RepairOrderList(ROId),
    FOREIGN KEY (MaterialId) REFERENCES Materials(MaterialId)
);

CREATE TABLE TaskLog (
    TaskId INT AUTO_INCREMENT PRIMARY KEY,
    RequestId INT,
    RequestTypeId INT,
    MaterialId INT,
    StaffId INT,
    Quantity INT,
    SlipCode VARCHAR(30) UNIQUE,
    CreatedAt DATETIME DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (RequestId) REFERENCES RequestList(RequestId),
    FOREIGN KEY (RequestTypeId) REFERENCES RequestType(RequestTypeId),
    FOREIGN KEY (MaterialId) REFERENCES Materials(MaterialId),
    FOREIGN KEY (StaffId) REFERENCES Users(UserId)
);

CREATE TABLE TaskSlipDetail (
    SlipId INT AUTO_INCREMENT PRIMARY KEY,
    TaskId INT,
    MaterialId INT,
    Quantity INT,
    FOREIGN KEY (TaskId) REFERENCES TaskLog(TaskId),
    FOREIGN KEY (MaterialId) REFERENCES Materials(MaterialId)
);

CREATE TABLE PasswordResetRequest (
    RequestId INT AUTO_INCREMENT PRIMARY KEY,
    UserId INT NOT NULL,
    RequestedAt DATETIME DEFAULT CURRENT_TIMESTAMP,
    Status VARCHAR(20) DEFAULT 'Pending',
    ProcessedAt DATETIME,
    FOREIGN KEY (UserId) REFERENCES Users(UserId)
);

CREATE TABLE Events (
    EventId INT AUTO_INCREMENT PRIMARY KEY,
    UserId INT NOT NULL,
    Title VARCHAR(255) NOT NULL,
    Description TEXT,
    StartTime DATETIME NOT NULL,
    EndTime DATETIME,
    EventType VARCHAR(50),
    CreatedAt DATETIME DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (UserId) REFERENCES Users(UserId)
);


CREATE TABLE ImportList (
    ImportId INT AUTO_INCREMENT PRIMARY KEY,
    ImportDate DATETIME DEFAULT CURRENT_TIMESTAMP,
    ImportedBy INT NOT NULL,
    Note TEXT,
    TotalValue DECIMAL(15,2) DEFAULT 0,
    Status VARCHAR(50) DEFAULT 'Completed',
    FOREIGN KEY (ImportedBy) REFERENCES Users(UserId)
);

-- Import Detail Table
CREATE TABLE ImportDetail (
    ImportDetailId INT AUTO_INCREMENT PRIMARY KEY,
    ImportId INT NOT NULL,
    MaterialId INT NOT NULL,
    Quantity INT NOT NULL,
    UnitPrice DECIMAL(10,2),
    TotalPrice DECIMAL(15,2),
    FOREIGN KEY (ImportId) REFERENCES ImportList(ImportId),
    FOREIGN KEY (MaterialId) REFERENCES Materials(MaterialId)
);

-- Export List Table
CREATE TABLE ExportList (
    ExportId INT AUTO_INCREMENT PRIMARY KEY,
    ExportDate DATETIME DEFAULT CURRENT_TIMESTAMP,
    ExportedBy INT NOT NULL,
    Note TEXT,
    TotalValue DECIMAL(15,2) DEFAULT 0,
    Status VARCHAR(50) DEFAULT 'Completed',
    FOREIGN KEY (ExportedBy) REFERENCES Users(UserId)
);

-- Export Detail Table
CREATE TABLE ExportDetail (
    ExportDetailId INT AUTO_INCREMENT PRIMARY KEY,
    ExportId INT NOT NULL,
    MaterialId INT NOT NULL,
    Quantity INT NOT NULL,
    UnitPrice DECIMAL(10,2),
    TotalPrice DECIMAL(15,2),
    FOREIGN KEY (ExportId) REFERENCES ExportList(ExportId),
    FOREIGN KEY (MaterialId) REFERENCES Materials(MaterialId)
);









