CREATE DATABASE quan_ly_vat_tu;
USE quan_ly_vat_tu;

CREATE TABLE Roles (
    RoleId INT PRIMARY KEY,
    RoleName VARCHAR(100) NOT NULL
);
CREATE TABLE Modules (
    ModuleId   INT AUTO_INCREMENT PRIMARY KEY,
    ModuleName VARCHAR(100) NOT NULL
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


CREATE TABLE Functions (
    FunctionId INT AUTO_INCREMENT PRIMARY KEY,
    FunctionName VARCHAR(100) NOT NULL
);
ALTER TABLE Functions
  ADD COLUMN ModuleId INT NULL,
  ADD FOREIGN KEY (ModuleId) REFERENCES Modules(ModuleId);

CREATE TABLE FunctionDetails (
    RoleId INT,
    FunctionId INT,
    PRIMARY KEY (RoleId, FunctionId),
    FOREIGN KEY (RoleId) REFERENCES Roles(RoleId),
    FOREIGN KEY (FunctionId) REFERENCES Functions(FunctionId)
);

CREATE TABLE Categories (
    CategoryId INT AUTO_INCREMENT PRIMARY KEY,
    CategoryName VARCHAR(255) NOT NULL
);

CREATE TABLE SubCategories (
    SubCategoryId INT AUTO_INCREMENT PRIMARY KEY,
    SubCategoryName VARCHAR(255) NOT NULL,
    CategoryId INT,
    FOREIGN KEY (CategoryId) REFERENCES Categories(CategoryId)
);

CREATE TABLE MaterialStatus (
    StatusId INT AUTO_INCREMENT PRIMARY KEY,
    StatusName VARCHAR(100)
);

CREATE TABLE Materials (
    MaterialId INT AUTO_INCREMENT PRIMARY KEY,
    MaterialName VARCHAR(300),
    SubCategoryId INT,
    StatusId INT,
    Image VARCHAR(255),
    Description TEXT,
    Quantity INT DEFAULT 0,
    MinQuantity INT DEFAULT 0,
    Price DOUBLE,
    CreatedAt DATETIME DEFAULT CURRENT_TIMESTAMP,
    UpdatedAt DATETIME,
    FOREIGN KEY (SubCategoryId) REFERENCES SubCategories(SubCategoryId),
    FOREIGN KEY (StatusId) REFERENCES MaterialStatus(StatusId)
);

CREATE TABLE ImportType (
    ImportTypeId INT AUTO_INCREMENT PRIMARY KEY,
    ImportTypeName VARCHAR(100) NOT NULL
);

CREATE TABLE ExportType (
    ExportTypeId INT PRIMARY KEY AUTO_INCREMENT,
    ExportTypeName VARCHAR(255) NOT NULL,
    Description TEXT
);

CREATE TABLE RequestType (
    RequestTypeId INT AUTO_INCREMENT PRIMARY KEY,
    RequestTypeName VARCHAR(100)
);


CREATE TABLE RequestStatus (
    StatusCode VARCHAR(20) PRIMARY KEY,
    Description VARCHAR(100)
);

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
    AssignedStaffId INT,
    IsUpdated BOOLEAN DEFAULT FALSE,
    FOREIGN KEY (RequestedBy) REFERENCES Users(UserId),
    FOREIGN KEY (RequestTypeId) REFERENCES RequestType(RequestTypeId),
    FOREIGN KEY (ApprovedBy) REFERENCES Users(UserId),
    FOREIGN KEY (AssignedStaffId) REFERENCES Users(UserId),
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


-- NHẬP KHO


CREATE TABLE ImportList (
    ImportId INT AUTO_INCREMENT PRIMARY KEY,
    ImportDate DATETIME DEFAULT CURRENT_TIMESTAMP,
    ImportedBy INT,
    ImportTypeId INT,
    Note TEXT,
    RequestId INT,
    FOREIGN KEY (ImportedBy) REFERENCES Users(UserId),
    FOREIGN KEY (ImportTypeId) REFERENCES ImportType(ImportTypeId),
    FOREIGN KEY (RequestId) REFERENCES RequestList(RequestId)
);

CREATE TABLE ImportDetail (
    ImportDetailId INT AUTO_INCREMENT PRIMARY KEY,
    ImportId INT,
    MaterialId INT,
    Quantity INT,
    Price DOUBLE,
    FOREIGN KEY (ImportId) REFERENCES ImportList(ImportId),
    FOREIGN KEY (MaterialId) REFERENCES Materials(MaterialId)
);


CREATE TABLE ExportList (
    ExportId INT AUTO_INCREMENT PRIMARY KEY,
    ExportDate DATETIME DEFAULT CURRENT_TIMESTAMP,
    ExportedBy INT,
    ExportTypeId INT,
    Note TEXT,
    RequestId INT,
    FOREIGN KEY (ExportedBy) REFERENCES Users(UserId),
    FOREIGN KEY (ExportTypeId) REFERENCES ExportType(ExportTypeId),
    FOREIGN KEY (RequestId) REFERENCES RequestList(RequestId)
);



CREATE TABLE ExportDetail (
    ExportDetailId INT AUTO_INCREMENT PRIMARY KEY,
    ExportId INT,
    MaterialId INT,
    Quantity INT,
    FOREIGN KEY (ExportId) REFERENCES ExportList(ExportId),
    FOREIGN KEY (MaterialId) REFERENCES Materials(MaterialId)
);


-- ========== NOTIFICATIONS ==========
CREATE TABLE Notifications (
    NotificationId INT AUTO_INCREMENT PRIMARY KEY,
    UserId INT NOT NULL,
    Message VARCHAR(255) NOT NULL,
    IsRead BOOLEAN DEFAULT FALSE,
    CreatedAt DATETIME DEFAULT CURRENT_TIMESTAMP,
    RequestId INT,
    FOREIGN KEY (UserId) REFERENCES Users(UserId),
    FOREIGN KEY (RequestId) REFERENCES RequestList(RequestId)
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

CREATE TABLE RepairList (
    RepairId INT AUTO_INCREMENT PRIMARY KEY,
    RequestId INT NOT NULL,
    RepairedBy INT,
    RepairDate DATETIME DEFAULT CURRENT_TIMESTAMP,
    Status VARCHAR(50),
    Note TEXT,
    FOREIGN KEY (RequestId) REFERENCES RequestList(RequestId),
    FOREIGN KEY (RepairedBy) REFERENCES Users(UserId)
);

CREATE TABLE RepairDetail (
    RepairDetailId INT AUTO_INCREMENT PRIMARY KEY,
    RepairId INT,
    MaterialId INT,
    Quantity INT,
    Price DECIMAL(15,2),
    FOREIGN KEY (RepairId) REFERENCES RepairList(RepairId),
    FOREIGN KEY (MaterialId) REFERENCES Materials(MaterialId)
);







INSERT INTO Roles (RoleId, RoleName)
VALUES 
(1, 'Warehouse Manager'),
(2, 'Warehouse Staff'),
(3, 'Director'),
(4, 'Company Staff');

INSERT INTO Modules (ModuleName)
VALUES 
  ('User Management'),
  ('Material Management'),
  ('Import/Export Materials'),
  ('Material Requests'),
  ('Statistics & Reports');


INSERT INTO Users (FullName, UserImage, Email, Phone, Password, RoleId, IsActive)
VALUES
-- Quản lý kho (1 người)
('Trần Quản Lý', 'warehousemanager.png','quanlyvattu4@gmail.com', '0900000000', 'quanly123', 1, TRUE),

-- Giám đốc công ty (1 người)
('Nguyễn Giám Đốc',  'director.png','giamdoc@example.com', '0911111111', 'giamdoc123', 3, TRUE),

-- Nhân viên kho (8 người)
('Nguyễn Văn A', 'warehousestaff.png', 'a1@example.com', '0900000001', 'pass1', 2, TRUE),
('Nguyễn Văn B',  'warehousestaff.png','a2@example.com', '0900000002', 'pass2', 2, TRUE),
('Nguyễn Văn C', 'warehousestaff.png', 'a3@example.com', '0900000003', 'pass3', 2, TRUE),
('Trần Thị D',  'warehousestaff.png','a4@example.com', '0900000004', 'pass4', 2, TRUE),
('Lê Văn E',  'warehousestaff.png','a5@example.com', '0900000005', 'pass5', 2, TRUE),
('Hoàng Văn F', 'warehousestaff.png', 'a6@example.com', '0900000006', 'pass6', 2, TRUE),
('Lý Thị G', 'warehousestaff.png', 'a7@example.com', '0900000007', 'pass7', 2, TRUE),
('Đào Văn H', 'warehousestaff.png', 'a8@example.com', '0900000008', 'pass8', 2, TRUE),

 ('Nguyễn Nhân Viên', 'companystaff.png', 'nhanvienkho8686@gmail.com', '0912345678', 'nhanvien123', 2, TRUE),


-- Nhân viên công ty (10 người)
('Ngô Thị I', 'companystaff.png', 'b1@example.com', '0900000011', 'pass11', 4, TRUE),
('Phạm Văn J', 'companystaff.png', 'b2@example.com', '0900000012', 'pass12', 4, TRUE),
('Đặng Thị K', 'companystaff.png', 'b3@example.com', '0900000013', 'pass13', 4, TRUE),
('Vũ Văn L', 'companystaff.png', 'b4@example.com', '0900000014', 'pass14', 4, TRUE),
('Cao Thị M', 'companystaff.png', 'b5@example.com', '0900000015', 'pass15', 4, TRUE),
('Bùi Văn N', 'companystaff.png', 'b6@example.com', '0900000016', 'pass16', 4, TRUE),
('Đỗ Thị O', 'companystaff.png', 'b7@example.com', '0900000017', 'pass17', 4, TRUE),
('Tống Văn P', 'companystaff.png', 'b8@example.com', '0900000018', 'pass18', 4, TRUE),
('Mai Thị Q', 'companystaff.png', 'b9@example.com', '0900000019', 'pass19', 4, TRUE),
('Lương Văn R', 'companystaff.png', 'b10@example.com', '0900000020', 'pass20', 4, TRUE);


INSERT INTO Functions (FunctionName)
VALUES
  ('View users'),                             -- 1
  ('Add user'),                               -- 2
  ('Delete user'),                            -- 3
  ('View user details'),                      -- 4
  ('Edit user details (including password)'), -- 5
  ('Toggle user status (active/inactive)'),   -- 6
  ('Assign user roles'),                      -- 7
  ('View material categories/subcategories'), -- 8
  ('Add new material category'),              -- 9
  ('View material list'),                     -- 10
  ('Add new material'),                       -- 11
  ('Delete material'),                        -- 12
  ('Edit material information'),              -- 13
  ('Export materials'),                       -- 14
  ('Print export form'),                      -- 15
  ('Import materials'),                       -- 16
  ('General material statistics'),            -- 17
  ('Request material export'),                -- 18
  ('Request material import'),                -- 19
  ('Request to purchase materials'),          -- 20
  ('Request material repair'),                -- 21
  ('Approve request (Director only)'),        -- 22
  ('Reject request (Director only)'),         -- 23
  ('View all requests (pending or approved)'),-- 24
  ('View exported materials by date'),        -- 25
  ('View imported materials by date'),        -- 26
  ('Export statistics'),                      -- 27
  ('Import statistics'),                      -- 28
  ('Stock inventory statistics'),             -- 29
  ('Purchased material statistics'),          -- 30
  ('Repaired material statistics'),           -- 31
  ('Cost statistics'),                        -- 32
  ('Request password reset'),                 -- 33
  ('Manage role-function assignments');       -- 34

UPDATE Functions SET ModuleId = 1 WHERE FunctionId IN (1,2,3,4,5,6,7,33,34);
UPDATE Functions SET ModuleId = 2 WHERE FunctionId IN (8,9,10,11,12,13);
UPDATE Functions SET ModuleId = 3 WHERE FunctionId IN (14,15,16);
UPDATE Functions SET ModuleId = 4 WHERE FunctionId IN (18,19,20,21,24);
UPDATE Functions SET ModuleId = 5 WHERE FunctionId IN (17,22,23,25,26,27,28,29,30,31,32);

	INSERT INTO FunctionDetails (RoleId, FunctionId)
	VALUES
	(1, 1), (1, 2), (1, 3), (1, 4), (1, 5), (1, 6), (1, 7),      -- RoleId = 1   Warehouse Manager  
	(1, 8), (1, 9), (1, 10), (1, 11), (1, 12), (1, 13),
	(1, 14), (1, 15), (1, 16), (1, 17),
	(1, 18), (1, 19), (1, 20), (1, 21),
	(1, 24), (1, 25), (1, 26),
	(1, 27), (1, 28), (1, 29),
	(1, 30), (1, 31), (1, 32),(1,33),(1,34);

INSERT INTO FunctionDetails (RoleId, FunctionId)
VALUES
(2, 8), (2, 9), (2, 10), (2, 11), (2, 12), (2, 13),				-- RoleId = 2   Warehouse Staff 
(2, 14), (2, 15), (2, 16), (2, 17),
(2, 18), (2, 19), (2, 20), (2, 21),
(2, 24), (2, 25), (2, 26),
(2, 27), (2, 28), (2, 29),
(2, 30), (2, 31), (2, 32);


INSERT INTO FunctionDetails (RoleId, FunctionId)
VALUES
(3, 21), -- Request material repair							-- RoleId = 3     Director
(3, 22), -- Approve request
(3, 23), -- Reject request
(3, 24), -- View all requests
(3, 25), (3, 26), -- View materials by date
(3, 27), (3, 28), (3, 29), -- Stats
(3, 30), (3, 31), (3, 32); -- Cost statistics



INSERT INTO FunctionDetails (RoleId, FunctionId)
VALUES
(4, 18), -- Request material export							-- RoleId = 4     Company Staff
(4, 19), -- Request material import
(4, 20), -- Request to purchase materials
(4, 21), -- Request material repair
(4, 24); -- View all requests

INSERT INTO Categories (CategoryName)
VALUES 
('Structural Materials'),         -- 1
('Finishing Materials'),          -- 2
('Insulation & Waterproofing'),   -- 3
('Mechanical & Electrical'),      -- 4
('Interior Decoration'),          -- 5
('Other Construction Materials'); -- 6


INSERT INTO SubCategories (SubCategoryName, CategoryId)
VALUES 
-- Structural Materials
('Concrete', 1),
('Structural Steel', 1),
('Bricks', 1),
('Building Stone', 1),
('Structural Timber', 1),

-- Finishing Materials
('Flooring Materials', 2),
('Wall Coverings', 2),
('Paints and Coatings', 2),
('Ceiling Materials', 2),

-- Insulation & Waterproofing
('Thermal Insulation', 3),
('Waterproofing Materials', 3),
('Sound Insulation', 3),

-- Mechanical & Electrical
('Electrical Systems', 4),
('Plumbing Systems', 4),
('HVAC Systems', 4),

-- Interior Decoration
('Wooden Furniture', 5),
('Decorative Materials', 5),
('Sanitary Equipment', 5),

-- Other Construction Materials
('Foundation Materials', 6),
('Roofing Materials', 6),
('Door and Window Materials', 6);

INSERT INTO MaterialStatus (StatusName)
VALUES ('New'), ('Used'), ('Damaged'); -- 1, 2, 3


INSERT INTO Materials (MaterialName, SubCategoryId, StatusId, Image, Description, Quantity, MinQuantity, Price)
VALUES
('Reinforced Concrete', 1, 1, 'reinforced-concrete.png', 'High strength concrete with steel reinforcement', 100, 20, 1200000),
('Lightweight Concrete', 1, 1, 'lightweight-concrete.png', 'Concrete with low density for non-load-bearing walls', 50, 10, 950000),
('I-Beam 200x100', 2, 1, 'i-beam-200x100.png', 'Standard structural steel I-beam', 200, 30, 1800000),
('H-Beam 300x300', 2, 1, 'h-beam-300x300.png', 'Heavy-duty H-beam', 150, 20, 3500000),
('Red Clay Brick', 3, 1, 'red-clay-brick.png', 'Traditional fired clay brick', 10000, 1000, 1200),
('Non-fired Brick', 3, 1, 'non-fired-brick.png', 'Eco-friendly construction brick', 8000, 800, 1500),
('Granite Stone', 4, 1, 'granite-stone.png', 'Used for flooring and wall cladding', 300, 30, 450000),
('Limestone Block', 4, 1, 'limestone-block.png', 'For foundation and support walls', 250, 25, 400000),
('Pine Timber', 5, 1, 'pine-timber.png', 'Used in structural framing', 500, 50, 700000),
('Oak Beam', 5, 1, 'oak-beam.png', 'Heavy-duty wood for decorative structure', 200, 20, 1100000),
('Ceramic Tile 60x60', 6, 1, 'ceramic-tile-60x60.png', 'Matte finish ceramic tile', 1000, 200, 80000),
('Laminate Flooring', 6, 1, 'laminate-flooring.png', 'Wood-pattern laminate', 600, 100, 120000),
('Wall Tile 30x60', 7, 1, 'wall-tile-30x60.png', 'Glossy finish wall tile', 800, 100, 65000),
('Wallpaper Roll', 7, 1, 'wallpaper-roll.png', 'Modern style wallpaper', 300, 50, 95000),
('Water-based Paint (White)', 8, 1, 'water-based-paint-white.png', 'High coverage, interior paint', 500, 50, 60000),
('Exterior Paint (Blue)', 8, 1, 'exterior-paint-blue.png', 'Weather-resistant paint', 300, 30, 75000),
('Gypsum Ceiling Board', 9, 1, 'gypsum-ceiling-board.png', 'Used for suspended ceilings', 400, 40, 120000),
('Aluminum Ceiling Panel', 9, 1, 'aluminum-ceiling-panel.png', 'Waterproof, reflective surface', 200, 20, 250000),
('Glass Wool Roll', 10, 1, 'glass-wool-roll.png', 'For thermal and acoustic insulation', 100, 10, 130000),
('EPS Foam Sheet', 10, 1, 'eps-foam-sheet.png', 'Lightweight insulation board', 300, 30, 95000),
('Waterproof Membrane', 11, 1, 'waterproof-membrane.png', 'Used for basement and roof waterproofing', 150, 15, 185000),
('Bituminous Coating', 11, 1, 'bituminous-coating.png', 'Used in underground waterproofing', 100, 10, 160000),
('Acoustic Foam Panel', 12, 1, 'acoustic-foam-panel.png', 'Used in studios and meeting rooms', 250, 25, 95000),
('Insulated Wallboard', 12, 1, 'insulated-wallboard.png', 'Double-layer board with insulation', 150, 15, 125000),
('Copper Cable 2x2.5mm', 13, 1, 'copper-cable-2x2.5mm.png', 'Used for home wiring', 1000, 100, 12000),
('PVC Conduit Pipe 20mm', 13, 1, 'pvc-conduit-pipe-20mm.png', 'Protects electrical wires', 800, 80, 18000),
('PPR Pipe 25mm', 14, 1, 'ppr-pipe-25mm.png', 'For hot water supply', 600, 60, 35000),
('PVC Elbow 90°', 14, 1, 'pvc-elbow-90.png', 'Connector for plumbing lines', 1000, 100, 5000),
('Flexible Air Duct', 15, 1, 'flexible-air-duct.png', 'Air distribution ducting', 300, 30, 78000),
('Ceiling Exhaust Fan', 15, 1, 'ceiling-exhaust-fan.png', 'Ventilation device for small rooms', 150, 15, 220000),
('Wooden Cabinet', 16, 1, 'wooden-cabinet.png', 'Wall-hung kitchen cabinet', 100, 10, 1800000),
('Office Desk', 16, 1, 'office-desk.png', 'Standard 1.2m desk', 150, 15, 1250000),
('3D Wall Panel', 17, 1, '3d-wall-panel.png', 'PVC decorative wall panel', 200, 20, 145000),
('Crown Moulding (3m)', 17, 1, 'crown-moulding-3m.png', 'Polystyrene decorative trim', 300, 30, 25000),
('Toilet Bowl Set', 18, 1, 'toilet-bowl-set.png', 'Flush toilet + seat + tank', 80, 8, 1450000),
('Wash Basin Ceramic', 18, 1, 'wash-basin-ceramic.png', 'White ceramic basin', 120, 12, 550000),
('Concrete Pile 300x300', 19, 1, 'concrete-pile-300x300.png', 'Used for deep foundations', 200, 20, 950000),
('River Sand', 19, 1, 'river-sand.png', 'Fine sand for concrete mix', 1000, 100, 450000),
('Clay Roof Tile', 20, 1, 'clay-roof-tile.png', 'Traditional curved tile', 1500, 150, 15000),
('Metal Roofing Sheet', 20, 1, 'metal-roofing-sheet.png', 'Zinc-aluminum coated', 500, 50, 180000),
('Aluminum Sliding Door', 21, 1, 'aluminum-sliding-door.png', '2-panel glass door', 100, 10, 2450000),
('uPVC Window 1x1m', 21, 1, 'upvc-window-1x1m.png', 'White-framed tilt window', 120, 12, 1350000);


INSERT INTO RequestStatus (StatusCode, Description)
VALUES 
('Pending', 'Waiting for approval'),
('Approved', 'Approved by director'),
('Rejected', 'Request has been rejected'),
('Completed', 'Successfull');


INSERT INTO ExportType (ExportTypeName, Description)
VALUES
('For Construction', 'Export materials for ongoing construction projects'),
('For Maintenance', 'Export for maintenance of company facilities'),
('For Equipment Repair', 'Export parts/materials for repairing machines and tools'),
('Internal Transfer', 'Export to move materials internally between warehouses or departments'),
('Sample Testing', 'Export material samples for testing or quality inspection'),
('Return Due to Error', 'Export items to return due to incorrect import or damage'),
('Clearance / Liquidation', 'Export leftover or obsolete materials');




INSERT INTO ImportType (ImportTypeName)
VALUES 
('New purchase'),             -- 1
('Returned from repair'),     -- 2
('Returned from usage');      -- 3

INSERT INTO RequestType (RequestTypeName)
VALUES 
('Export'),
('Import'),
('Purchase'),
('Repair');





-- ========================================
-- 1. BẢNG REQUEST & DETAIL
-- ========================================

INSERT INTO RequestList 
(RequestId, RequestedBy, RequestDate, RequestTypeId, Note, Status, ApprovedBy, ApprovedDate, ApprovalNote, AssignedStaffId) 
VALUES
(1, 10, '2025-05-20 08:00:00', 2, 'Request to import materials for Project A', 'Approved', 2, '2025-05-20 08:30:00', 'Approved - match delivery note.', 3),
(2, 11, '2025-05-21 09:30:00', 2, 'Returned items after repair', 'Approved', 2, '2025-05-21 10:00:00', 'Valid receipt', 4),
(3, 12, '2025-05-25 07:30:00', 1, 'Export for foundation of Building A', 'Approved', 2, '2025-05-25 08:00:00', 'Approved on schedule', 5),
(4, 13, '2025-05-26 08:30:00', 1, 'Export finishing materials to site B', 'Approved', 2, '2025-05-26 09:00:00', 'Export approved.', 6),
(5, 4, NOW(), 4, 'Purchase office air conditioners', 'Approved', 2, NOW(), 'Approved within budget.', 7),
(6, 5, NOW(), 1, 'Export leftover cement', 'Rejected', 2, NOW(), 'Not needed. Keep for future use.', NULL),
(7, 6, NOW(), 3, 'Repair broken drill machine', 'Rejected', 2, NOW(), 'Request denied. Out of budget.', NULL),
(8, 7, NOW(), 2, 'Import bricks for building site', 'Pending', NULL, NULL, NULL, NULL),
(9, 8, NOW(), 3, 'Repair water leakage at storage', 'Pending', NULL, NULL, NULL, NULL),
(10, 14, '2025-05-29 10:00:00', 3, 'Propose purchase of office chairs', 'Pending', NULL, NULL, NULL, NULL),
(11, 4, '2025-06-15 09:00:00', 2, 'Import lightweight bricks', 'Approved', 2, '2025-06-15 10:00:00', 'OK to proceed.', NULL),
(12, 5, '2025-06-15 10:30:00', 1, 'Export granite stone to site D', 'Approved', 2, '2025-06-15 11:00:00', 'Site D confirmed.', 3),
(13, 6, '2025-06-16 08:15:00', 4, 'Repair concrete cutter', 'Approved', 2, '2025-06-16 08:45:00', 'Assigned for repair.', 5),
(14, 7, '2025-06-16 09:30:00', 3, 'Purchase new safety helmets', 'Pending', NULL, NULL, NULL, NULL),
(15, 8, '2025-06-17 08:45:00', 2, 'Import ceiling boards', 'Approved', 2, '2025-06-17 09:15:00', 'Approved for import.', 4),
(16, 9, '2025-06-17 10:20:00', 1, 'Export timber for site C', 'Approved', 2, '2025-06-17 10:50:00', 'Export confirmed.', NULL),
(17, 10, '2025-06-18 09:00:00', 4, 'Repair lighting system', 'Approved', 2, '2025-06-18 09:30:00', 'Assigned to technician.', 6),
(18, 11, '2025-06-18 11:00:00', 3, 'Purchase fire extinguishers', 'Approved', 2, '2025-06-18 11:20:00', 'Urgent purchase approved.', 7),
(19, 12, '2025-06-19 08:00:00', 1, 'Export wall tiles to branch B', 'Pending', NULL, NULL, NULL, NULL),
(20, 13, '2025-06-19 10:00:00', 2, 'Import steel rods for base', 'Approved', 2, '2025-06-19 10:30:00', 'Warehouse notified.', NULL);

-- RequestDetail (đầy đủ vật tư cho từng RequestId)
INSERT INTO RequestDetail (RequestId, MaterialId, Quantity, ActualQuantity) VALUES
-- RequestId = 1 (Import)
(1, 1, 50, NULL),
(1, 2, 30, NULL),
(1, 3, 20, NULL),

-- RequestId = 2 (Import + Purchase)
(2, 21, 10, NULL),
(2, 18, 30, NULL),

-- RequestId = 3 (Export)
(3, 1, 20, NULL),
(3, 3, 10, NULL),

-- RequestId = 4 (Export)
(4, 11, 100, NULL),
(4, 13, 50, NULL),

-- RequestId = 5 (Purchase)
(5, 15, 3, NULL),
(5, 11, 150, NULL),

-- RequestId = 6 (Export)
(6, 16, 50, NULL),

-- RequestId = 7 (Repair)
(7, 17, 1, NULL),
(7, 3, 2, NULL),

-- RequestId = 8 (Import)
(8, 18, 200, NULL),

-- RequestId = 9 (Repair)
(9, 19, 1, NULL),

-- RequestId = 10 (Purchase)
(10, 25, 5, NULL),

-- RequestId = 11 (Import)
(11, 6, 500, NULL),

-- RequestId = 12 (Export)
(12, 7, 50, NULL),

-- RequestId = 13 (Repair)
(13, 3, 1, NULL),

-- RequestId = 14 (Purchase)
(14, 35, 20, NULL),

-- RequestId = 15 (Import)
(15, 17, 200, NULL),

-- RequestId = 16 (Export)
(16, 9, 80, NULL),

-- RequestId = 17 (Repair)
(17, 26, 1, NULL),

-- RequestId = 18 (Purchase)
(18, 28, 30, NULL),

-- RequestId = 19 (Export)
(19, 13, 100, NULL),

-- RequestId = 20 (Import)
(20, 3, 100, NULL);



-- ========================================
-- 2. NHẬP KHO
-- ========================================

INSERT INTO ImportList (ImportId, ImportDate, ImportedBy, ImportTypeId, Note, RequestId) VALUES
(1, '2025-05-20 09:00:00', 3, 1, 'Imported materials for Project A', 1),
(2, '2025-05-21 10:30:00', 4, 2, 'Returned after external repair', 2),
(3, NOW(), 3, 1, '500 steel bars for new project', 8),
(4, '2025-06-15 14:00:00', 3, 1, 'Bricks delivered from supplier', 11),
(5, '2025-06-17 12:00:00', 4, 1, 'Ceiling boards for office site', 15),
(6, '2025-06-19 14:00:00', 3, 1, 'Steel rods imported for base foundation', 20);

-- ImportDetail
INSERT INTO ImportDetail (ImportId, MaterialId, Quantity, Price) VALUES
(1, 1, 0, 1200000), (1, 2, 0, 950000), (1, 3, 0, 1800000),
(2, 21, 0, 220000), (2, 18, 0, 550000),
(4, 6, 0, 1500), (5, 17, 0, 120000), (6, 3, 0, 1800000);

-- ========================================
-- 3. XUẤT KHO
-- ========================================

INSERT INTO ExportList (ExportId, ExportDate, ExportedBy, ExportTypeId, Note, RequestId) VALUES
(1, '2025-05-25 09:00:00', 3, 1, 'Export for foundation of Building A', 3),
(2, '2025-05-26 10:45:00', 4, 1, 'Export finishing materials to site B', 4),
(3, NOW(), 5, 5, 'Leftover cement not needed, export for clearance', 6),
(4, '2025-06-15 13:00:00', 3, 1, 'Granite stone to Site D', 12),
(5, '2025-06-17 13:30:00', 4, 1, 'Timber for site C foundation', 16),
(6, '2025-06-19 15:30:00', 3, 1, 'Export wall tiles to branch B', 19);

-- ExportDetail
INSERT INTO ExportDetail (ExportId, MaterialId, Quantity) VALUES
(1, 1, 0), (1, 3, 0), (2, 11, 0), (2, 13, 0),
(4, 7, 0), (5, 9, 0),
(6, 13, 0);

-- ========================================
-- 4. PHIẾU MUA HÀNG
-- ========================================

INSERT INTO PurchaseOrderList (RequestId, CreatedBy, CreatedDate, TotalPrice, Status, Note)
VALUES
(2, 12, NOW(), 1100000, 'Pending', 'Đề nghị mua Ceiling Exhaust Fan'),
(5, 8, NOW(), 12000000, 'Pending', 'Đề nghị mua gạch lát nền'),
(18, 11, NOW(), 6600000, 'Pending', 'Purchase fire extinguishers urgently');

INSERT INTO PurchaseOrderDetail (POId, MaterialId, Quantity, UnitPrice, Total) VALUES
(1, 21, 0, 220000, 0),
(2, 11, 0, 80000, 0),
(3, 28, 0, 220000, 0);
-- ========================================
-- 5. SỬA CHỮA
-- ========================================

INSERT INTO RepairList (RequestId, RepairedBy, RepairDate, Status, Note)
VALUES
(7, 6, NOW(), 'In Progress', 'Đang tháo lắp để kiểm tra nguyên nhân'),
(13, 5, '2025-06-16 11:00:00', 'In Progress', 'Inspection ongoing.'),
(17, 6, '2025-06-18 13:00:00', 'Pending', 'Awaiting parts.');

-- RepairDetail
INSERT INTO RepairDetail (RepairId, MaterialId, Quantity, Price) VALUES
(1, 3, 0, 500000),
(2, 3, 0, 1800000),
(3, 26, 0, 160000);


-- ========================================
-- 6. CALENDAR/SCHEDULING TABLES
-- ========================================

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

