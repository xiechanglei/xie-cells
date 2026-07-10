import {Box, Flex, HStack, IconButton, Input, Separator, Spacer, Text, VStack} from "@chakra-ui/react"
import {Outlet} from "react-router-dom"
import {LuBell, LuChevronLeft, LuChevronRight, LuLayoutGrid, LuSearch, LuSettings, LuShield, LuUser, LuUsers} from "react-icons/lu"
import {FiActivity} from "react-icons/fi";
import {Avatar} from "@/components/ui/avatar"
import {ColorModeButton} from "@/components/ui/color-mode"
import {
    MenuContent,
    MenuItem,
    MenuRoot,
    MenuSeparator,
    MenuTrigger,
} from "@/components/ui/menu"
import {Tooltip} from "@/components/ui/tooltip"
import {InputGroup} from "@/components/ui/input-group"
import {useState} from "react"

/** 顶部导航菜单项 */
const NAV_ITEMS = [
    {label: "首页", path: "/"},
    {label: "用户管理", path: "/users"},
    {label: "角色管理", path: "/roles"},
    {label: "权限管理", path: "/permissions"},
    {label: "系统设置", path: "/settings"},
]

/** 侧边栏菜单项 */
const SIDEBAR_ITEMS = [
    {icon: FiActivity, label: "概览", path: "/"},
    {icon: LuUsers, label: "用户管理", path: "/users"},
    {icon: LuShield, label: "角色管理", path: "/roles"},
    {icon: LuSettings, label: "权限管理", path: "/permissions"},
    {icon: LuLayoutGrid, label: "系统设置", path: "/settings"},
]

/**
 * 用户登陆之后的程序主框架，框架使用四区域分栏设计：
 * - 顶部：固定高度导航栏（左侧Logo + 中间导航菜单 + 右侧搜索/主题/通知/用户）
 * - 左侧：可折叠侧边栏菜单
 * - 中间：主内容区域
 * - 底部：状态栏
 * @constructor
 */
const Main = () => {
    const [sidebarCollapsed, setSidebarCollapsed] = useState(false)

    return (
        <Flex direction="column" h="100vh" overflow="hidden">
            {/* ====== 顶部导航栏 ====== */}
            <Flex as="header" align="center" h="16" px="4" bg="bg.panel" borderBottomWidth="1px" borderColor="border" zIndex="docked" flexShrink={0}>
                {/* 左侧：Logo */}
                <HStack gap="2" mr="6">
                    <Box w="8" h="8" borderRadius="lg" bg="colorPalette.solid" colorPalette="blue" display="flex" alignItems="center" justifyContent="center">
                        <LuLayoutGrid size={18} color="white"/>
                    </Box>
                    <Text fontSize="lg" fontWeight="bold" letterSpacing="tight">RBAC Manager</Text>
                </HStack>

                {/* 中间：导航菜单 */}
                <HStack gap="1" as="nav" display={{base: "none", md: "flex"}}>
                    {NAV_ITEMS.map(item => (
                        <Box key={item.path} px="3" py="1.5" borderRadius="md" cursor="pointer" fontSize="sm" fontWeight="medium" color="fg.muted" _hover={{bg: "bg.muted", color: "fg"}} transition="all 0.15s">
                            {item.label}
                        </Box>
                    ))}
                </HStack>

                <Spacer/>

                {/* 右侧：功能区域 */}
                <HStack gap="1">
                    {/* 搜索 */}
                    <InputGroup startElement={<LuSearch size={16}/>} maxW="200px" display={{lg: "flex"}}>
                        <Input placeholder="搜索功能..." size="sm" borderRadius="full" variant="subtle"/>
                    </InputGroup>

                    {/* 主题切换 */}
                    <ColorModeButton/>

                    {/* 通知 */}
                    <Tooltip content="通知消息">
                        <IconButton variant="ghost" size="sm" aria-label="通知">
                            <LuBell size={18}/>
                        </IconButton>
                    </Tooltip>

                    {/* 用户头像 & 下拉菜单 */}
                    <MenuRoot>
                        <MenuTrigger asChild>
                            <IconButton variant="ghost" size="sm" aria-label="用户菜单" rounded="full">
                                <Avatar name="Admin" size="xs" icon={<LuUser/>}/>
                            </IconButton>
                        </MenuTrigger>
                        <MenuContent>
                            <MenuItem value="profile">
                                <LuUser size={16}/>
                                个人中心
                            </MenuItem>
                            <MenuItem value="settings">
                                <LuSettings size={16}/>
                                账号设置
                            </MenuItem>
                            <MenuSeparator/>
                            <MenuItem value="logout" color="fg.error">
                                退出登录
                            </MenuItem>
                        </MenuContent>
                    </MenuRoot>
                </HStack>
            </Flex>

            {/* ====== 中间区域：左侧边栏 + 右侧内容 ====== */}
            <Flex flex="1" overflow="hidden">
                {/* 左侧：可折叠侧边栏 */}
                <Flex as="aside" direction="column" w={sidebarCollapsed ? "14" : "56"} bg="bg.panel" borderRightWidth="1px" borderColor="border" transition="width 0.2s ease" flexShrink={0} overflow="hidden">
                    {/* 折叠/展开按钮 */}
                    <Flex justify="flex-end" p="2">
                        <IconButton variant="ghost" size="sm" aria-label={sidebarCollapsed ? "展开侧边栏" : "收起侧边栏"} onClick={() => setSidebarCollapsed(!sidebarCollapsed)}>
                            {sidebarCollapsed ? <LuChevronRight size={16}/> : <LuChevronLeft size={16}/>}
                        </IconButton>
                    </Flex>

                    <Separator mb="2"/>

                    {/* 侧边栏菜单 */}
                    <VStack gap="1" px="2" align="stretch" flex="1" overflowY="auto">
                        {SIDEBAR_ITEMS.map(item => {
                            const Icon = item.icon
                            return (
                                <HStack
                                    px="3"
                                    py="2"
                                    borderRadius="md"
                                    cursor="pointer"
                                    color="fg.muted"
                                    _hover={{bg: "bg.muted", color: "fg"}}
                                    transition="all 0.15s"
                                    justify={sidebarCollapsed ? "center" : "flex-start"}
                                >
                                    <Icon size={18}/>
                                    {!sidebarCollapsed && (
                                        <Text fontSize="sm" fontWeight="medium">
                                            {item.label}
                                        </Text>
                                    )}
                                </HStack>
                            )
                        })}
                    </VStack>
                </Flex>

                {/* 右侧：主内容区域 */}
                <Box as="main" flex="1" overflow="auto" bg="bg.subtle" p="4">
                    <Outlet/>
                </Box>
            </Flex>
        </Flex>
    )
}

export default Main