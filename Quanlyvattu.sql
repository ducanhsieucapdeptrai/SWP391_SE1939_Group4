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
    MaterialName VARCHAR(255),
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

-- NHẬP KHO
CREATE TABLE ImportType (
    ImportTypeId INT AUTO_INCREMENT PRIMARY KEY,
    ImportTypeName VARCHAR(100) NOT NULL
);

CREATE TABLE ImportList (
    ImportId INT AUTO_INCREMENT PRIMARY KEY,
    ImportDate DATETIME DEFAULT CURRENT_TIMESTAMP,
    ImportedBy INT,
    ImportTypeId INT,                  -- Thêm trường này
    Note TEXT,
    FOREIGN KEY (ImportedBy) REFERENCES Users(UserId),
    FOREIGN KEY (ImportTypeId) REFERENCES ImportType(ImportTypeId)
);



CREATE TABLE ImportDetail (
    ImportDetailId INT AUTO_INCREMENT PRIMARY KEY,      -- Khóa chính riêng biệt
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
    Note TEXT,
    FOREIGN KEY (ExportedBy) REFERENCES Users(UserId)
);


CREATE TABLE ExportDetail (
    ExportDetailId INT AUTO_INCREMENT PRIMARY KEY,       -- Khóa chính riêng
    ExportId INT,
    MaterialId INT,
    Quantity INT,
    FOREIGN KEY (ExportId) REFERENCES ExportList(ExportId),
    FOREIGN KEY (MaterialId) REFERENCES Materials(MaterialId)
);


-- YÊU CẦU VẬT TƯ
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
    FOREIGN KEY (RequestedBy) REFERENCES Users(UserId),
    FOREIGN KEY (RequestTypeId) REFERENCES RequestType(RequestTypeId),
    FOREIGN KEY (ApprovedBy) REFERENCES Users(UserId)
);


INSERT INTO RequestStatus (StatusCode, Description)
VALUES 
('Pending', 'Waiting for approval'),
('Approved', 'Approved by director'),
('Rejected', 'Request has been rejected');

ALTER TABLE RequestList 
MODIFY COLUMN Status VARCHAR(20),
ADD CONSTRAINT FK_Request_Status 
FOREIGN KEY (Status) REFERENCES RequestStatus(StatusCode);

CREATE TABLE RequestDetail (
    RequestId INT,
    MaterialId INT,
    Quantity INT,
    PRIMARY KEY (RequestId, MaterialId),
    FOREIGN KEY (RequestId) REFERENCES RequestList(RequestId),
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


INSERT INTO Users (FullName, Email, Phone, Password, RoleId, IsActive)
VALUES
-- Quản lý kho (1 người)
('Trần Quản Lý', 'quanlyvattu4@gmail.com', '0900000000', 'quanly123', 1, TRUE),

-- Giám đốc công ty (1 người)
('Nguyễn Giám Đốc', 'giamdoc@example.com', '0911111111', 'giamdoc123', 3, TRUE),

-- Nhân viên kho (8 người)
('Nguyễn Văn A', 'a1@example.com', '0900000001', 'pass1', 2, TRUE),
('Nguyễn Văn B', 'a2@example.com', '0900000002', 'pass2', 2, TRUE),
('Nguyễn Văn C', 'a3@example.com', '0900000003', 'pass3', 2, TRUE),
('Trần Thị D', 'a4@example.com', '0900000004', 'pass4', 2, TRUE),
('Lê Văn E', 'a5@example.com', '0900000005', 'pass5', 2, TRUE),
('Hoàng Văn F', 'a6@example.com', '0900000006', 'pass6', 2, TRUE),
('Lý Thị G', 'a7@example.com', '0900000007', 'pass7', 2, TRUE),
('Đào Văn H', 'a8@example.com', '0900000008', 'pass8', 2, TRUE),

-- Nhân viên công ty (10 người)
('Ngô Thị I', 'b1@example.com', '0900000011', 'pass11', 4, TRUE),
('Phạm Văn J', 'b2@example.com', '0900000012', 'pass12', 4, TRUE),
('Đặng Thị K', 'b3@example.com', '0900000013', 'pass13', 4, TRUE),
('Vũ Văn L', 'b4@example.com', '0900000014', 'pass14', 4, TRUE),
('Cao Thị M', 'b5@example.com', '0900000015', 'pass15', 4, TRUE),
('Bùi Văn N', 'b6@example.com', '0900000016', 'pass16', 4, TRUE),
('Đỗ Thị O', 'b7@example.com', '0900000017', 'pass17', 4, TRUE),
('Tống Văn P', 'b8@example.com', '0900000018', 'pass18', 4, TRUE),
('Mai Thị Q', 'b9@example.com', '0900000019', 'pass19', 4, TRUE),
('Lương Văn R', 'b10@example.com', '0900000020', 'pass20', 4, TRUE);


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
('Reinforced Concrete', 1, 1, 'assets/images/materials/reinforced-concrete.png', 'High strength concrete with steel reinforcement', 100, 20, 1200000),
('Lightweight Concrete', 1, 1, 'assets/images/materials/lightweight-concrete.png', 'Concrete with low density for non-load-bearing walls', 50, 10, 950000),
('I-Beam 200x100', 2, 1, 'assets/images/materials/i-beam-200x100.png', 'Standard structural steel I-beam', 200, 30, 1800000),
('H-Beam 300x300', 2, 1, 'assets/images/materials/h-beam-300x300.png', 'Heavy-duty H-beam', 150, 20, 3500000),
('Red Clay Brick', 3, 1, 'assets/images/materials/red-clay-brick.png', 'Traditional fired clay brick', 10000, 1000, 1200),
('Non-fired Brick', 3, 1, 'assets/images/materials/non-fired-brick.png', 'Eco-friendly construction brick', 8000, 800, 1500),
('Granite Stone', 4, 1, 'assets/images/materials/granite-stone.png', 'Used for flooring and wall cladding', 300, 30, 450000),
('Limestone Block', 4, 1, 'assets/images/materials/limestone-block.png', 'For foundation and support walls', 250, 25, 400000),
('Pine Timber', 5, 1, 'assets/images/materials/pine-timber.png', 'Used in structural framing', 500, 50, 700000),
('Oak Beam', 5, 1, 'assets/images/materials/oak-beam.png', 'Heavy-duty wood for decorative structure', 200, 20, 1100000),
('Ceramic Tile 60x60', 6, 1, 'assets/images/materials/ceramic-tile-60x60.png', 'Matte finish ceramic tile', 1000, 200, 80000),
('Laminate Flooring', 6, 1, 'assets/images/materials/laminate-flooring.png', 'Wood-pattern laminate', 600, 100, 120000),
('Wall Tile 30x60', 7, 1, 'assets/images/materials/wall-tile-30x60.png', 'Glossy finish wall tile', 800, 100, 65000),
('Wallpaper Roll', 7, 1, 'assets/images/materials/wallpaper-roll.png', 'Modern style wallpaper', 300, 50, 95000),
('Water-based Paint (White)', 8, 1, 'assets/images/materials/water-based-paint-white.png', 'High coverage, interior paint', 500, 50, 60000),
('Exterior Paint (Blue)', 8, 1, 'assets/images/materials/exterior-paint-blue.png', 'Weather-resistant paint', 300, 30, 75000),
('Gypsum Ceiling Board', 9, 1, 'assets/images/materials/gypsum-ceiling-board.png', 'Used for suspended ceilings', 400, 40, 120000),
('Aluminum Ceiling Panel', 9, 1, 'assets/images/materials/aluminum-ceiling-panel.png', 'Waterproof, reflective surface', 200, 20, 250000),
('Glass Wool Roll', 10, 1, 'assets/images/materials/glass-wool-roll.png', 'For thermal and acoustic insulation', 100, 10, 130000),
('EPS Foam Sheet', 10, 1, 'assets/images/materials/eps-foam-sheet.png', 'Lightweight insulation board', 300, 30, 95000),
('Waterproof Membrane', 11, 1, 'assets/images/materials/waterproof-membrane.png', 'Used for basement and roof waterproofing', 150, 15, 185000),
('Bituminous Coating', 11, 1, 'assets/images/materials/bituminous-coating.png', 'Used in underground waterproofing', 100, 10, 160000),
('Acoustic Foam Panel', 12, 1, 'assets/images/materials/acoustic-foam-panel.png', 'Used in studios and meeting rooms', 250, 25, 95000),
('Insulated Wallboard', 12, 1, 'assets/images/materials/insulated-wallboard.png', 'Double-layer board with insulation', 150, 15, 125000),
('Copper Cable 2x2.5mm', 13, 1, 'assets/images/materials/copper-cable-2x2.5mm.png', 'Used for home wiring', 1000, 100, 12000),
('PVC Conduit Pipe 20mm', 13, 1, 'assets/images/materials/pvc-conduit-pipe-20mm.png', 'Protects electrical wires', 800, 80, 18000),
('PPR Pipe 25mm', 14, 1, 'assets/images/materials/ppr-pipe-25mm.png', 'For hot water supply', 600, 60, 35000),
('PVC Elbow 90°', 14, 1, 'assets/images/materials/pvc-elbow-90.png', 'Connector for plumbing lines', 1000, 100, 5000),
('Flexible Air Duct', 15, 1, 'assets/images/materials/flexible-air-duct.png', 'Air distribution ducting', 300, 30, 78000),
('Ceiling Exhaust Fan', 15, 1, 'assets/images/materials/ceiling-exhaust-fan.png', 'Ventilation device for small rooms', 150, 15, 220000),
('Wooden Cabinet', 16, 1, 'assets/images/materials/wooden-cabinet.png', 'Wall-hung kitchen cabinet', 100, 10, 1800000),
('Office Desk', 16, 1, 'assets/images/materials/office-desk.png', 'Standard 1.2m desk', 150, 15, 1250000),
('3D Wall Panel', 17, 1, 'assets/images/materials/3d-wall-panel.png', 'PVC decorative wall panel', 200, 20, 145000),
('Crown Moulding (3m)', 17, 1, 'assets/images/materials/crown-moulding-3m.png', 'Polystyrene decorative trim', 300, 30, 25000),
('Toilet Bowl Set', 18, 1, 'assets/images/materials/toilet-bowl-set.png', 'Flush toilet + seat + tank', 80, 8, 1450000),
('Wash Basin Ceramic', 18, 1, 'assets/images/materials/wash-basin-ceramic.png', 'White ceramic basin', 120, 12, 550000),
('Concrete Pile 300x300', 19, 1, 'assets/images/materials/concrete-pile-300x300.png', 'Used for deep foundations', 200, 20, 950000),
('River Sand', 19, 1, 'assets/images/materials/river-sand.png', 'Fine sand for concrete mix', 1000, 100, 450000),
('Clay Roof Tile', 20, 1, 'assets/images/materials/clay-roof-tile.png', 'Traditional curved tile', 1500, 150, 15000),
('Metal Roofing Sheet', 20, 1, 'assets/images/materials/metal-roofing-sheet.png', 'Zinc-aluminum coated', 500, 50, 180000),
('Aluminum Sliding Door', 21, 1, 'assets/images/materials/aluminum-sliding-door.png', '2-panel glass door', 100, 10, 2450000),
('uPVC Window 1x1m', 21, 1, 'assets/images/materials/upvc-window-1x1m.png', 'White-framed tilt window', 120, 12, 1350000);




INSERT INTO ImportType (ImportTypeName)
VALUES 
('New purchase'),             -- 1
('Returned from repair'),     -- 2
('Returned from usage');      -- 3

INSERT INTO ImportList (ImportDate, ImportedBy, ImportTypeId, Note)
VALUES
('2025-05-20 09:00:00', 3, 1, 'Imported structural materials for Project A'),         -- Mua vật tư xây thô
('2025-05-21 10:30:00', 4, 1, 'Imported finishing materials for model house'),        -- Mua vật tư hoàn thiện
('2025-05-22 14:00:00', 3, 2, 'Returned from repair after external servicing'),       -- Vật tư sửa chữa mang về
('2025-05-23 08:45:00', 5, 3, 'Returned borrowed materials from field work'),         -- Vật tư mượn trả lại
('2025-05-24 11:15:00', 4, 1, 'Purchased additional MEP items');                      -- Mua vật tư điện nước


INSERT INTO ImportDetail (ImportId, MaterialId, Quantity, Price)
VALUES
-- ImportId = 1: Structural materials
(1, 1, 50, 1200000),     -- Reinforced Concrete
(1, 2, 30, 950000),      -- Lightweight Concrete
(1, 3, 20, 1800000),     -- I-Beam
(1, 5, 1000, 1200),      -- Red Brick

-- ImportId = 2: Finishing materials
(2, 11, 500, 80000),     -- Ceramic Tile
(2, 13, 100, 60000),     -- Water-based Paint
(2, 15, 100, 120000),    -- Gypsum Ceiling Board

-- ImportId = 3: Returned from repair
(3, 21, 10, 220000),     -- Ceiling Exhaust Fan
(3, 18, 30, 550000),     -- Wash Basin

-- ImportId = 4: Returned from usage
(4, 8, 20, 450000),      -- Granite Stone
(4, 10, 50, 700000),     -- Pine Timber

-- ImportId = 5: Electrical & plumbing
(5, 25, 100, 12000),     -- Copper Cable
(5, 27, 80, 35000);      -- PPR Pipe

INSERT INTO ExportList (ExportDate, ExportedBy, Note)
VALUES
('2025-05-25 09:00:00', 3, 'Export for concrete foundation of Building A'),            -- Xuất vật tư móng
('2025-05-26 10:45:00', 4, 'Export finishing materials to site B'),                    -- Xuất vật tư hoàn thiện
('2025-05-27 14:20:00', 3, 'Tools exported for maintenance'),                          -- Xuất vật tư bảo trì
('2025-05-28 08:30:00', 5, 'Returned materials issued for trial build'),               -- Xuất thử nghiệm
('2025-05-29 11:00:00', 4, 'Daily dispatch of electrical supplies');                   -- Xuất thiết bị điện

INSERT INTO RequestType (RequestTypeName)
VALUES 
('Material Export'),        -- 1
('Material Import'),        -- 2
('Material Purchase'),      -- 3
('Material Repair');        -- 4

INSERT INTO RequestList (RequestedBy, RequestTypeId, Note, Status)
VALUES
(10, 1, 'Request cement and steel for foundation', 'Pending'),            -- xuất kho
(12, 3, 'Propose purchase of ceiling fans', 'Pending'),                   -- đề nghị mua
(11, 4, 'Request repair of broken water pump', 'Pending'),                -- đề nghị sửa
(9, 1, 'Request bricks for wall construction', 'Pending'),                -- xuất kho
(8, 2, 'Request to return unused floor tiles to warehouse', 'Pending');   -- nhập kho

INSERT INTO RequestDetail (RequestId, MaterialId, Quantity)
VALUES
(1, 1, 20),     -- Reinforced Concrete
(1, 3, 10),     -- I-Beam

(2, 21, 5),     -- Ceiling Exhaust Fan

(3, 27, 1),     -- PPR Pipe (giả định hỏng)

(4, 5, 1000),   -- Red Brick

(5, 11, 150);   -- Ceramic Tile

INSERT INTO RequestType (RequestTypeName)
VALUES 
('Material Export'),        -- 1
('Material Import'),        -- 2
('Material Purchase'),      -- 3
('Material Repair');        -- 4

INSERT INTO RequestList (RequestedBy, RequestTypeId, Note, Status)
VALUES
(10, 1, 'Request cement and steel for foundation', 'Pending'),            -- Export
(12, 3, 'Propose purchase of ceiling fans', 'Pending'),                   -- Purchase
(11, 4, 'Request repair of broken water pump', 'Pending'),                -- Repair
(9, 1, 'Request bricks for wall construction', 'Pending'),                -- Export
(8, 2, 'Request to return unused tiles to warehouse', 'Pending');         -- Import

-- Tổng số vật tư
SELECT COUNT(*) AS TotalMaterials FROM Materials;

-- Số lượt nhập kho trong tháng này
SELECT COUNT(*) AS TotalImportsThisMonth
FROM ImportList
WHERE MONTH(ImportDate) = MONTH(CURDATE())
  AND YEAR(ImportDate) = YEAR(CURDATE());

-- Số lượt xuất kho trong tháng này
SELECT COUNT(*) AS TotalExportsThisMonth
FROM ExportList
WHERE MONTH(ExportDate) = MONTH(CURDATE())
  AND YEAR(ExportDate) = YEAR(CURDATE());

-- Số yêu cầu đang chờ xử lý
SELECT COUNT(*) AS PendingRequests
FROM RequestList
WHERE Status = 'Pending';

SELECT u.*, r.RoleName
FROM Users u
JOIN Roles r ON u.RoleId = r.RoleId
WHERE u.Email = 'giamdoc@example.com' AND u.Password = 'giamdoc123';






